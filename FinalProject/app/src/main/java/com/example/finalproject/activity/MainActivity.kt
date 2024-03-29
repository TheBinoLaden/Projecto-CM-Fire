package com.example.finalproject.activity

import android.Manifest
import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.finalproject.R
import com.example.finalproject.activity.address.AddressActivity
import com.example.finalproject.activity.occurrence.AddOccurrenceActivity
import com.example.finalproject.activity.occurrence.ListNewOccurrenceActivity
import com.example.finalproject.activity.usercontrol.SettingsActivity
import com.example.finalproject.misc.adapter.CustomInfoWindowAdapter
import com.example.finalproject.misc.helperclasses.Occurrence
import com.example.finalproject.misc.weather.District
import com.example.finalproject.utils.AddressUtils
import com.example.finalproject.utils.OccurrencesUtils
import com.example.finalproject.utils.StringUtils
import com.example.finalproject.utils.WeatherUtils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    lateinit var floatingButton: FloatingActionButton
    lateinit var txtInfo: TextView
    lateinit var fireRiskText: TextView
    lateinit var fireRiskDetails: TextView
    private lateinit var timerAlert: CountDownTimer

    //Variaveis para teste de notificacao
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATIO_ID = 0

    //Variaveis para a localização do utilizador
    private var lastLocation: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationGranted = false
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private const val ZOOM = 18F
    }

    //Variaveis do teste do alarme de incendio
    private lateinit var dialogFire: AlertDialog

    private var storeMarkers: ArrayList<MarkerOptions>? = null

    // Stores Occurrences when the user moves but the Database remains the same
    private var storeOccurrences: ArrayList<Occurrence>? = null
    private var listOccurrencesNotification: ArrayList<String>? = null


    // FCUL is the defaultLocation
    private val defaultLocation = LatLng(38.75648904803744, -9.155400218408356)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //adicionar lista de pontos
        storeMarkers = ArrayList()
        storeOccurrences = ArrayList()
        listOccurrencesNotification = ArrayList()

        toolbar = findViewById(R.id.myToolBar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navView.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.textView7)
        val userName = intent.extras?.getString("username") ?: ""
        name.text = userName



        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_map -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("username", name.text)
                    startActivity(intent)
                }
                R.id.nav_adress -> {
                    val intent = Intent(this, AddressActivity::class.java)
                    intent.putExtra("username", name.text)
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra("username", name.text)
                    startActivity(intent)
                }
                R.id.nav_occurrence -> {
                    val intent = Intent(this, ListNewOccurrenceActivity::class.java)
                    intent.putExtra("username", name.text)
                    startActivity(intent)
                }

            }
            true
        }

        addListenerOfDatabase()
        getUserAddress(userName)

        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////               MAP RELATED LOGIC                     //////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Inicializar o mapa
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //Codigo do botão de adicionar ocurrencias
        floatingButton = findViewById(R.id.btn_addProblem)
        floatingButton.setOnClickListener {
            //Colocar pop-up para adicionar ocorrencia
            val intent = Intent(this, AddOccurrenceActivity::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("latitude", lastLocation!!.latitude)
            intent.putExtra("longitude", lastLocation!!.longitude)
            startActivity(intent)
        }

        // Fire Risk card view and details
        val fireRiskCardView: CardView = findViewById(R.id.fire_risk_card)
        fireRiskText = findViewById(R.id.fire_risk_text)
        fireRiskDetails = findViewById(R.id.fire_risk_text_details)
        val fireRiskLayout: LinearLayout = findViewById(R.id.fire_risk_layout)
        fireRiskLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        fireRiskCardView.setOnClickListener {
            val vis = if (fireRiskDetails.visibility == View.GONE) View.VISIBLE else View.GONE
            TransitionManager.beginDelayedTransition(fireRiskLayout, AutoTransition())
            fireRiskDetails.visibility = vis
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////               MENU HELPER FUNCTIONS                 //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)


//        val searchView = search?.actionView as SearchView
//        searchView.queryHint = "Search Address"

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Menu lateral
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        //Ativa o popup dos filtros
        if (item.itemId == R.id.filter) {
            val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.bottom_filters,
                findViewById<LinearLayout>(R.id.bottomFiltersContainer)
            )

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            val fireOption = bottomSheetDialog.findViewById<CheckBox>(R.id.cb_incendio)
            val workOption = bottomSheetDialog.findViewById<CheckBox>(R.id.cb_manutencao)
            val btnFilter = bottomSheetDialog.findViewById<Button>(R.id.btn_applyFilter)
            btnFilter!!.setOnClickListener {
                if (fireOption!!.isChecked && !workOption!!.isChecked) {
                    filters("Incendio")
                } else if (workOption!!.isChecked && !fireOption.isChecked) {
                    filters("Manutencao")
                } else {
                    filters("Tudo")
                }
            }
        }

        if (item.itemId == R.id.search) {
            if (!Places.isInitialized()) {
                Places.initialize(applicationContext, getString(R.string.api_key2), Locale.getDefault())
            }

            val AUTOCOMPLETE_REQUEST_CODE = 1

            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = listOf(Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).setCountry("PT").build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        if (place.latLng != null)
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng!!, 13f));
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("TESTE", status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun createNewNotification(actualLocation: LatLng) {
        OccurrencesUtils.searchOccurrencesList { documents ->
            for (document in documents) {
                val latLon = document["coordinates"] as HashMap<String, Double>
                val lat = latLon["lat"] as Double
                val lon = latLon["lon"] as Double
                val type = document["type"] as String

                val coord = lat.toString() + ";" + lon.toString()
                val positionOccurrence = LatLng(lat, lon)

                var alredyExist = 0
                val distance = calculateDistance(positionOccurrence, actualLocation)

                for (r in listOccurrencesNotification!!.indices) {
                    if (listOccurrencesNotification!![r] == coord) {
                        alredyExist = 1
                    }
                }
                Log.d("tag","distance: " + distance.toString())
                if (alredyExist == 0) {
                    if (type == "Incendio") {
                        if (distance <= 5) {
                            Thread.sleep(1000)
                            showDialogNormal()
                            listOccurrencesNotification!!.add(lat.toString() + ";" + lon.toString())
                        }
                    } else if (type == "Manutencao") {
                        val info =
                            "Manutenção de Terreno" + ";" + "Existe um terreno perto de si que necessita de manutenção"

                        if (distance <= 5) {
                            createNotificationChannel()
                            createNotification(info)
                            listOccurrencesNotification!!.add(lat.toString() + ";" + lon.toString())
                        }
                    }
                }
            }
        }
    }

    private fun filters(filtro: String) {
        googleMap.clear()
        storeMarkers!!.clear()

        OccurrencesUtils.searchOccurrencesList { documents ->
            for (document in documents) {
                val latLon = document["coordinates"] as HashMap<String, Double>
                val lat = latLon["lat"] as Double
                val lon = latLon["lon"] as Double
                val titleOc = document["title"] as String
                val type = document["type"] as String
                val des = document["description"] as String
                var smallMarkerIcon: BitmapDescriptor? = null

                val coordenates = LatLng(lat, lon)
                val morada = AddressUtils.getAddressFromLocation(this, coordenates)

                val info = "Morada: " + morada + "\nDescrição: "+ des

                if (filtro == "Tudo") {
                    if (type == "Incendio") {
                        smallMarkerIcon = iconMap(0)
                    } else if (type == "Manutencao") {
                        smallMarkerIcon = iconMap(1)
                    }

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(coordenates)
                            .icon(smallMarkerIcon)
                            .title(titleOc)
                            .snippet(info)
                    )

                    storeMarker(
                        MarkerOptions()
                            .position(coordenates)
                            .icon(smallMarkerIcon)
                            .title(titleOc)
                            .snippet(info)
                    )
                } else {
                    if (type == filtro) {
                        if (filtro == "Incendio") {
                            smallMarkerIcon = iconMap(0)

                        } else if (filtro == "Manutencao") {
                            smallMarkerIcon = iconMap(1)
                        }
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(coordenates)
                                .icon(smallMarkerIcon)
                                .title(titleOc)
                                .snippet(info)
                        )

                        storeMarker(
                            MarkerOptions()
                                .position(coordenates)
                                .icon(smallMarkerIcon)
                                .title(titleOc)
                                .snippet(info)
                        )
                    }
                }
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////               NOTIFICATION HELPER FUNCTIONS         //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    //Código teste alarme Incendio perto
    private fun showDialogNormal() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.customdialogfirealarm, null)

        build.setView(view)

        dialogFire = build.create()
        dialogFire.setCanceledOnTouchOutside(false)
        dialogFire.show()

        //Testar contador
        timerAlert = object : CountDownTimer(3_000, 1_000) {
            override fun onTick(remain: Long) {
            }

            override fun onFinish() {
                dialogFire.cancel()
            }

        }
        timerAlert.start()
    }

    private fun checkWork(lat: String, long: String, actualLocation: LatLng, information: String) {

        val coord = LatLng(lat.toDouble(), long.toDouble())
        val distance = calculateDistance(coord, actualLocation)

        if (distance <= 5) {
            createNotificationChannel()
            createNotification(information)
            listOccurrencesNotification!!.add("$lat;$long")
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(information: String) {
        val intent = Intent(this, MainActivity::class.java)
        var pendingIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        val str = ";"
        val parts = information.split(str)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(parts[0])
            .setContentText(parts[1])
            .setSmallIcon(R.drawable.fireicon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(NOTIFICATIO_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////               MAP HELPER FUNCTIONS                  //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    private fun updateLocationUI() {
        try {
            if (locationGranted) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                lastLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    // Double check against the permission
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationGranted = true
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 34)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getFirstDeviceLocation() {
        try {
            if (locationGranted) {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastLocation = task.result
                        if (lastLocation != null) {
                            // private val defaultLocation = LatLng(-33.8523341, 151.2106085)
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastLocation!!.latitude,
                                        lastLocation!!.longitude
                                    ), ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(defaultLocation, ZOOM.toFloat())
                        )
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Security Exception: %s", e.message, e)
        }
    }

    // initialize the locationRequest with the Priority and Interval of update
    private fun setLocationFetchSettings() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
    }

    // store the marker into the array
    private fun storeMarker(marker: MarkerOptions) {
        storeMarkers?.add(marker)
    }

    // For each marker in the array, fills the map with it
    private fun setMarkers() {
        for (marker in storeMarkers!!) {
            googleMap.addMarker(marker)
            googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        }
    }

    // Gets the user address
    private fun getUserAddress(user: String) {
        AddressUtils.getFavAddress(user) { favAddress ->
            for (address in favAddress) {
                val addressName = address["Address"] as String
                val description = address["Description"] as String
                val latLon = address["coordinates"] as HashMap<String, Double>
                val lat = latLon["lat"] as Double
                val lon = latLon["lon"] as Double
                val coord = LatLng(lat.toDouble(), lon.toDouble())

                notificationAddress(addressName, coord)
                Log.d("tag", addressName + " - " + lat.toString() + ";" + lon.toString())
            }
        }
    }

    // for each occurrence avaliable notifies the user if not already notified
    private fun notificationAddress(morada: String, coordAddress: LatLng) {
        for (i in storeOccurrences!!.indices) {
            val str = ";"
            val parts = storeOccurrences!![i]
            val lat = parts.occurrenceAddress.latitude
            val lon = parts.occurrenceAddress.longitude
            val coord = lat + str + lon
            var alreadyExist = 0

            for (r in listOccurrencesNotification!!.indices) {
                if (listOccurrencesNotification!![r] == coord) {
                    alreadyExist = 1
                }
            }

            if (alreadyExist == 0) {
                val info = "Incendio" + ";" + "Existe um incendio perto da sua morada " + morada
                checkWork(
                    parts.occurrenceAddress.latitude, parts.occurrenceAddress.longitude,
                    coordAddress, info
                )
            }
        }

    }

    //Código para calcular distancia
    private fun calculateDistance(pointLocation: LatLng, actualLocation: LatLng): Int {
        var final = 0.0f
        val results = FloatArray(3)
        Location.distanceBetween(
            actualLocation.latitude,
            actualLocation.longitude,
            pointLocation.latitude,
            pointLocation.longitude,
            results)

        final = results[0] / 1000

        return final.toInt()
        //Log.d("tag",String.format("%.1f",results[0]/1000) + "km")

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        //Localização do utilizador
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        // Updates the UI location settings
        updateLocationUI()
        // Gets the first location of the device
        getFirstDeviceLocation()
        setUpMap()

        OccurrencesUtils.searchOccurrencesList { documents ->
            for (document in documents) {
                val latLon = document["coordinates"] as HashMap<String, Double>
                val lat = latLon["lat"] as Double
                val lon = latLon["lon"] as Double
                val type = document["type"] as String
                val titleOc = document["title"] as String
                val des = document["description"] as String
                var smallMarkerIcon: BitmapDescriptor? = null

                val coordinates = LatLng(lat, lon)
                val morada = AddressUtils.getAddressFromLocation(this, coordinates)
                val info = "Morada: " + morada + "\nDescrição: "+ des

                if (type == "Incendio") {
                    smallMarkerIcon = iconMap(0)
                } else if (type == "Manutencao") {
                    smallMarkerIcon = iconMap(1)
                }

                googleMap.addMarker(
                    MarkerOptions()
                        .position(coordinates)
                        .icon(smallMarkerIcon)
                        .title(titleOc)
                        .snippet(info)
                )

                storeMarker(
                    MarkerOptions()
                        .position(coordinates)
                        .icon(smallMarkerIcon)
                        .title(titleOc)
                        .snippet(info)
                )

                googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
            }
        }

        // Quando o user para de dar drag da camera, dá update das coordenadas e da info do distrito
        googleMap.setOnCameraIdleListener()
        {
            val target = googleMap.cameraPosition.target
            val latLng = LatLng(target.latitude, target.longitude)
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses!!.isNotEmpty()) {
                if (addresses[0] != null && addresses[0].adminArea != null) {
                    val district = District.getDistrict(addresses[0].adminArea)
                    if (district != null) {
                        WeatherUtils.getDistrictWeather(district) { dataSet ->
                            WeatherUtils.checkIfShouldUpdate(
                                district, latLng.latitude, latLng.longitude,
                                dataSet["lastUpdate"] as Long
                            )
                            val newRisk = "Fire Risk: " + dataSet["fireRisk"] as String
                            val newDetails =
                                "Temperature: " + DecimalFormat("#.##").format(dataSet["temp"] as Double) + "C" +
                                        "\nHumidity: " + DecimalFormat("#.##").format(dataSet["humidity"] as Double) + "%" +
                                        "\nWind Speed: " + DecimalFormat("#.##").format(dataSet["windSpeed"] as Double) + "m/s" +
                                        "\nWind Rotation: " + dataSet["windDeg"] as Long
                            fireRiskText.text = newRisk
                            fireRiskDetails.text = newDetails
                        }
                    }
                }
            }
        }
    }

    private fun iconMap(type: Int): BitmapDescriptor? {
        var bitmap: Bitmap? = null
        if (type == 0) {
            val marker = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.map_marker_fire,
                null
            )
            val cardView = marker.findViewById<CardView>(R.id.markerFireIcon)
            bitmap = Bitmap.createScaledBitmap(
                viewToBitmap(cardView)!!,
                cardView.width,
                cardView.height,
                false
            )

        } else if (type == 1) {
            val marker = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.map_marker_work,
                null
            )
            val cardView = marker.findViewById<CardView>(R.id.markerWorkIcon)
            bitmap = Bitmap.createScaledBitmap(
                viewToBitmap(cardView)!!,
                cardView.width,
                cardView.height,
                false
            )
        }
        val smallMarkerIcon = bitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
        return smallMarkerIcon
    }

    private fun viewToBitmap(view: View): Bitmap? {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE
            )
        }

        setLocationFetchSettings()

        // Este callback permite apanhar as alteracoes de localizacao
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lastLocation = locationResult.lastLocation
                if (locationResult.locations.size != 0
                    && (locationResult.locations[locationResult.locations.size - 1] != lastLocation
                            || locationResult.locations.size == 1)
                ) {
                    val currentLatLong =
                        lastLocation?.let { LatLng(it.latitude, lastLocation!!.longitude) }

                    if (currentLatLong != null) {
                        googleMap.clear()
                        setMarkers()
                        createNewNotification(currentLatLong)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////       DATABASE LISTENER HELPER FUNCTIONS            //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

    private fun addListenerOfDatabase() {
        val dbConnection = Firebase.firestore
        val docRef = dbConnection.collection("occurrences")

        docRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                for (document in it) {
                    val convertedString = StringUtils.makeStringFromDatabase(document)
                    val convertedOccurrence = OccurrencesUtils.makeOccurrenceFromDatabase(document)
                    Log.d("tag", convertedString)
                    storeOccurrences?.add(convertedOccurrence)
                }
            }
        }
    }
}
package com.example.finalproject.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.finalproject.CustomInfoWindowAdapter
import com.example.finalproject.R
import com.example.finalproject.activity.address.AddressActivity
import com.example.finalproject.activity.occurrence.ListNewOccurrenceActivity
import com.example.finalproject.activity.occurrence.OccurrenceActivity
import com.example.finalproject.activity.usercontrol.SettingsActivity
import com.example.finalproject.enums.Tags
import com.example.finalproject.weather.APIData
import com.example.finalproject.weather.Formulas
import com.example.finalproject.weather.Model
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    lateinit var floatingButton: FloatingActionButton
    lateinit var txtInfo: TextView

    //Variaveis para a localização do utilizador
    private var lastLocation: Location? = null
    var testePosicao = LatLng(0.0, 0.0)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationGranted = false
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 15
    }

    //Variaveis do teste do alarme de incendio
    private lateinit var dialogFire: AlertDialog

    //Variaveis de teste dos pontos no mapa
    val ponto1 = LatLng(38.589607, -9.154542)
    val ponto2 = LatLng(38.589846, -9.154051)
    private var testeLocais: ArrayList<LatLng>? = null
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //adicionar lista de pontos
        testeLocais = ArrayList()
        testeLocais!!.add(ponto1)
        testeLocais!!.add(ponto2)

        //Teste da Morada Escrita
        val testeMoradaEscrita = getAddressCoordenates("Rua Serra de Nisa 4")
        val testeEscrita =
            testeMoradaEscrita.latitude.toString() + "," + testeMoradaEscrita.longitude.toString()

        //testeLocais!!.add(testeMoradaEscrita)
        //Fim do Teste

        toolbar = findViewById(R.id.myToolBar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navView.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.textView7)
        name.text = intent.extras?.getString("username") ?: ""

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

        //Inicializar o mapa
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //Codigo do botão de adicionar ocurrencias
        floatingButton = findViewById(R.id.btn_addProblem)
        txtInfo = findViewById(R.id.txt_risk)
        floatingButton.setOnClickListener {
            //Colocar pop-up para adicionar ocorrencia
            startActivity(Intent(this, OccurrenceActivity::class.java))
        }

        // API LIMITS: 60 calls/min; 1.000.000 calls/month
        // Air pollution API is also free, possibility to use
        val testLat = 38.75665F
        val testLon = -9.155493F
        val units = "metric" // Units available: https://openweathermap.org/current#data
        val lang = "pt" // Languages available by prefix: https://openweathermap.org/current#multi
        APIData.getData(testLat, testLon, units, lang, object : APIData.Response {
            override fun onResponse(data: Model.Result) {
                val desc: String = data.weather[0].description
                val temp: Float = data.main.temp
                val humidity: Float = data.main.humidity
//                val rainVolume: Float = data.rain[0].oneHour
                val country: String = data.sys.country
                val windSpeed: Float = data.wind.speed
                val windDeg: Int = data.wind.deg
//                Log.d("APICALL",
//                            "country: $country\n" +
//                            "desc: $desc\n" +
//                            "temp: $temp\n" +
//                            "windSpeed: $windSpeed\n" +
//                            "windDegrees: $windDeg")
                Log.d("APICALL", data.toString())
                val fdi = Formulas.fireDangerIndex(temp, humidity, 2f, windSpeed)
                Log.d("APICALL", "Fire Danger Index: $fdi")
                txtInfo.text = "Risco: ${Formulas.fdiInterpretation(fdi)}"
            }

            override fun onFailure(error: Throwable) {
                Log.e("APICALL", "Could not make call to weather API", error)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        //Abre a aba do search
        val search: MenuItem? = menu?.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search"

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
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        //Localização do utilizador
        googleMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()

        //Código para adicionar os pontos
        for (i in testeLocais!!.indices) {

            val morada = getAddressName(testeLocais!![i].latitude, testeLocais!![i].longitude)

            if (i == 0) {
                val marker = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.map_marker_fire,
                    null
                )
                val cardView = marker.findViewById<CardView>(R.id.markerFireIcon)
                val bitmap = Bitmap.createScaledBitmap(
                    viewToBitmap(cardView)!!,
                    cardView.width,
                    cardView.height,
                    false
                )
                val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)

                googleMap.addMarker(
                    MarkerOptions()
                        .position(testeLocais!![i])
                        .icon(smallMarkerIcon)
                        .title("Definição")
                        .snippet(morada)
                )
            } else {
                val marker = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.map_marker_work,
                    null
                )
                val cardView = marker.findViewById<CardView>(R.id.markerWorkIcon)
                val bitmap = Bitmap.createScaledBitmap(
                    viewToBitmap(cardView)!!,
                    cardView.width,
                    cardView.height,
                    false
                )
                val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)

                googleMap.addMarker(
                    MarkerOptions()
                        .position(testeLocais!![i])
                        .icon(smallMarkerIcon)
                        .title("Definição")
                        .snippet(morada)
                )
            }
            //calculateDistance(testeLocais!![i])
            googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))

        }

        updateLocationUI()

        getDeviceLocation()
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }

        setLocationFetchSettings()


        // Este callback permite apanhar as alteracoes de localizacao
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lastLocation = locationResult.lastLocation
                if (locationResult.locations.size != 0
                    && (locationResult.locations[locationResult.locations.size - 1] != lastLocation || locationResult.locations.size == 1)
                ) {
                    Log.d(Tags.COORDINATES.name, lastLocation?.latitude.toString())
                    Log.d(Tags.COORDINATES.name, lastLocation?.longitude.toString())
                    Log.d(Tags.COORDINATES.name, lastLocation?.accuracy.toString())

                    val currentLatLong =
                        lastLocation?.let { LatLng(it.latitude, lastLocation!!.longitude) }

                    if (currentLatLong != null) {
                        googleMap.clear()
                        placeMarkerLocation(currentLatLong)
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLatLong,
                                18F
                            )
                        )
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        checkFire()

    }

    private fun placeMarkerLocation(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("Estou Aqui")
        googleMap.addMarker(markerOptions)
    }

    //Função permite obter o morada através de coordenadas
    private fun getAddressName(lat: Double, lon: Double): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat, lon, 1)

        //val address = addressList!![0].getAddressLine(0)
        val address = addressList!![0].getAddressLine(0) + "\n Para mais informações, clique"
        return address
    }

    //Função permite obter coordenadas através de morada
    private fun getAddressCoordenates(address: String): com.google.android.gms.maps.model.LatLng {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val cAddress = geoCoder.getFromLocationName(address, 1)

        val location: Address = cAddress!!.get(0)
        val d = LatLng(location.latitude, location.longitude)
        return d
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

    //Código teste alarme Incendio perto
    private fun showDialogNormal() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.customdialogfirealarm, null)

        build.setView(view)

        dialogFire = build.create()
        dialogFire.show()
    }

    //Código para calcular distancia
    private fun calculateDistance(pointLocation: LatLng) {

        val results = FloatArray(3)
        Location.distanceBetween(
            testePosicao.latitude,
            testePosicao.longitude,
            pointLocation.latitude,
            pointLocation.longitude,
            results
        )

        val final = results[0] / 1000

        if (final < 5) {
            showDialogNormal()
        }
        //Log.d("tag",(results[0]*0.001).toString() + "km")
        //Log.d("tag",String.format("%.1f",results[0]/1000) + "km")

    }

    private fun checkFire() {

        for (i in testeLocais!!.indices) {
            if (i == 0) {
                calculateDistance(testeLocais!![i])
            }
        }

    }

    private fun updateLocationUI() {
        try {
            if (locationGranted) {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                googleMap.isMyLocationEnabled = false
                googleMap.uiSettings.isMyLocationButtonEnabled = false
                lastLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    // Double check against the permission
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                34
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
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
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        googleMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        googleMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun setLocationFetchSettings() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
    }


}
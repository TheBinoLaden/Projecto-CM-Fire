package com.example.finalproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.finalproject.R
import com.example.finalproject.activity.address.AddressActivity
import com.example.finalproject.activity.occurrence.ListNewOccurrenceActivity
import com.example.finalproject.activity.occurrence.OccurrenceActivity
import com.example.finalproject.activity.usercontrol.SettingsActivity
import com.example.finalproject.weather.APIData
import com.example.finalproject.weather.Model
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    lateinit var floatingButton: FloatingActionButton
    lateinit var txtInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    intent.putExtra("favPlaces", this.intent.extras?.getString("favPlaces") ?: "")
                    startActivity(intent)
                }
                R.id.nav_adress -> {
                    val intent = Intent(this, AddressActivity::class.java)
                    intent.putExtra("username", name.text)
                    intent.putExtra("favPlaces", this.intent.extras?.getString("favPlaces") ?: "")
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra("username", name.text)
                    intent.putExtra("favPlaces", this.intent.extras?.getString("favPlaces") ?: "")
                    startActivity(intent)
                }
                R.id.nav_occurrence -> {
                    val intent = Intent(this, ListNewOccurrenceActivity::class.java)
                    intent.putExtra("username", name.text)
                    intent.putExtra("favPlaces", this.intent.extras?.getString("favPlaces") ?: "")
                    startActivity(intent)
                }

            }
            true
        }

        //Inicializar o mapa
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
        })

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

}
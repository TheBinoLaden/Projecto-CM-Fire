package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap : GoogleMap
    lateinit var floatingButton : FloatingActionButton
    lateinit var txtInfo : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.myToolBar)
        setSupportActionBar(toolbar)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_adress -> startActivity(Intent(this,AdressActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this,SettingsActivity::class.java))
                R.id.nav_occurrence -> startActivity(Intent(this,ListNewOccurrenceActivity::class.java))
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
            //Colocar popup para adicionar ocurrencia
            startActivity(Intent(this,OccurrenceActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)

        //Abre a aba do search
        val search : MenuItem? = menu?.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search"

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Menu lateral
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        //Ativa o popup dos filtros
        if(item.itemId == R.id.filter){
            val bottomSheetDialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.bottom_filters,findViewById<LinearLayout>(R.id.bottomFiltersContainer))

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        return super.onOptionsItemSelected(item)
    }
}
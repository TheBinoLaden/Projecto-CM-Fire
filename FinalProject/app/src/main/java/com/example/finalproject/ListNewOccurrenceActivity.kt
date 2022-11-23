package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ListNewOccurrenceActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_new_occurrence)

        toolbar=findViewById(R.id.myToolBar2)
        setSupportActionBar(toolbar)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerOccur)
        val navView : NavigationView = findViewById(R.id.nav_view02)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_map -> startActivity(Intent(this,MainActivity::class.java))
                R.id.nav_adress -> startActivity(Intent(this,AdressActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this,SettingsActivity::class.java))
                R.id.nav_occurrence -> startActivity(Intent(this,ListNewOccurrenceActivity::class.java))
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
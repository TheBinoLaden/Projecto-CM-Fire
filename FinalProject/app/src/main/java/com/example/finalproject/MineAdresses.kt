package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class MineAdresses : AppCompatActivity() {

    lateinit var adressFragment: AdressFragment
    lateinit var toolbarInfo : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_adresses)

        adressFragment = AdressFragment()

        toolbarInfo = findViewById(R.id.myToolBar)
        toolbarInfo.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        val fragment : Fragment? = supportFragmentManager.findFragmentByTag(AdressFragment::class.java.simpleName)
        if(fragment !is AdressFragment){
            supportFragmentManager.beginTransaction().add(R.id.table_adresses, adressFragment, AdressFragment::class.java.simpleName)
        }

    }
}


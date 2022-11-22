package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MineAdresses : AppCompatActivity() {

    lateinit var adressFragment: AdressFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_adresses)

        adressFragment = AdressFragment()

        val fragment : Fragment? = supportFragmentManager.findFragmentByTag(AdressFragment::class.java.simpleName)
        if(fragment !is AdressFragment){
            supportFragmentManager.beginTransaction().add(R.id.table_adresses, adressFragment, AdressFragment::class.java.simpleName)
        }

    }
}


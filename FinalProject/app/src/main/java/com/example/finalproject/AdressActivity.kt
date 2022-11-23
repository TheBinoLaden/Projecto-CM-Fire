package com.example.finalproject

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class AdressActivity : AppCompatActivity() {

    lateinit var addAdressButton : FloatingActionButton
    lateinit var description: String
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adresses)

        toolbar=findViewById(R.id.myToolBar2)
        setSupportActionBar(toolbar)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerAdress)
        val navView : NavigationView = findViewById(R.id.nav_view01)

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

        //PopUp adicionar Morada
        addAdressButton = findViewById(R.id.btn_addAdress)
        addAdressButton.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_adress,null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("Adicionar Morada")
            val mAlertDialog = mBuilder.show()

            mDialogView.findViewById<Button>(R.id.btn_dialogConfirm).setOnClickListener {
                mAlertDialog.dismiss()
                //Obter os dados do formulário
                description = mAlertDialog.findViewById<EditText>(R.id.dialogDefinition)?.text.toString()
                Toast.makeText(this, description,Toast.LENGTH_LONG).show()
            }

            mDialogView.findViewById<Button>(R.id.btn_dialogCancel).setOnClickListener {
                mAlertDialog.dismiss()
                Toast.makeText(this,"Cancelou",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}


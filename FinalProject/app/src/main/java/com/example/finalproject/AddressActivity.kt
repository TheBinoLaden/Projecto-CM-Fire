package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.example.finalproject.occurrence.ListNewOccurrenceActivity
import com.example.finalproject.usercontrol.SettingsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class AddressActivity : AppCompatActivity() {

    lateinit var addAddressButton : FloatingActionButton
    lateinit var description: String
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var designacao: TextView
    lateinit var morada: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adresses)

        designacao = findViewById(R.id.casa)
        morada = findViewById(R.id.morada)

        toolbar=findViewById(R.id.myToolBar2)
        setSupportActionBar(toolbar)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerAdress)
        val navView : NavigationView = findViewById(R.id.nav_view01)
        val header: View = navView.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.textView7)
        name.text = intent.extras?.getString("username") ?: ""



        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_map -> startActivity(Intent(this,MainActivity::class.java))
                R.id.nav_adress -> startActivity(Intent(this,AddressActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.nav_occurrence -> startActivity(Intent(this, ListNewOccurrenceActivity::class.java))
            }
            true
        }

        //PopUp adicionar Morada
        addAddressButton = findViewById(R.id.btn_addAdress)
        addAddressButton.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_adress,null)
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

        designacao.setOnClickListener {
            val mDialogView1 = LayoutInflater.from(this).inflate(R.layout.dialog_info_adress,null)
            val mBuilder1 = AlertDialog.Builder(this).setView(mDialogView1).setTitle("Informação da Morada")
            val mAlertDialog1 = mBuilder1.show()

            mDialogView1.findViewById<Button>(R.id.btn_dialogEdit).setOnClickListener {
                mAlertDialog1.dismiss()
                //Obter os dados do formulário
                Toast.makeText(this, "Editou",Toast.LENGTH_LONG).show()
            }

            mDialogView1.findViewById<Button>(R.id.btn_dialogDelet).setOnClickListener {
                mAlertDialog1.dismiss()
                Toast.makeText(this,"Apagou",Toast.LENGTH_LONG).show()
            }
        }

        morada.setOnClickListener {
            val mDialogView1 = LayoutInflater.from(this).inflate(R.layout.dialog_info_adress,null)
            val mBuilder1 = AlertDialog.Builder(this).setView(mDialogView1).setTitle("Informação da Morada")
            val mAlertDialog1 = mBuilder1.show()

            mDialogView1.findViewById<Button>(R.id.btn_dialogEdit).setOnClickListener {
                mAlertDialog1.dismiss()
                //Obter os dados do formulário
                Toast.makeText(this, "Editou",Toast.LENGTH_LONG).show()
            }

            mDialogView1.findViewById<Button>(R.id.btn_dialogDelet).setOnClickListener {
                mAlertDialog1.dismiss()
                Toast.makeText(this,"Apagou",Toast.LENGTH_LONG).show()
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


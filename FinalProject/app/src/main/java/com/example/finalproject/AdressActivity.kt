package com.example.finalproject

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdressActivity : AppCompatActivity() {

    lateinit var adressFragment: AdressFragment
    lateinit var toolbarInfo : Toolbar
    lateinit var addAdressButton : FloatingActionButton
    lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adresses)

        adressFragment = AdressFragment()

        goBack()

        val fragment : Fragment? = supportFragmentManager.findFragmentByTag(AdressFragment::class.java.simpleName)
        if(fragment !is AdressFragment){
            supportFragmentManager.beginTransaction().add(R.id.table_adresses, adressFragment, AdressFragment::class.java.simpleName)
        }

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

    //função para o botão de back na toolbar
    fun goBack(){
        toolbarInfo = findViewById(R.id.myToolBar)
        toolbarInfo.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

}


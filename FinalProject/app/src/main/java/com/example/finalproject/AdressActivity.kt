package com.example.finalproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class AdressActivity : AppCompatActivity() {

    lateinit var addAddressButton: FloatingActionButton
    lateinit var description: String
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adresses)
        findViewById<ComposeView>(R.id.my_composable).setContent {
            /**TODO replace the string list with the info from DB
             * A class can be created to hold more info (i.e. address.name, address.temp, etc)
             */
            ListAnimationComponent(listOf("Rua Test", "Rua Test2", "Rua Test3"))
        }

        toolbar = findViewById(R.id.myToolBar2)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerAdress)
        val navView: NavigationView = findViewById(R.id.nav_view01)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_map -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_adress -> startActivity(Intent(this, AdressActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.nav_occurrence -> startActivity(
                    Intent(
                        this,
                        ListNewOccurrenceActivity::class.java
                    )
                )
            }
            true
        }

        //PopUp adicionar Morada
        addAddressButton = findViewById(R.id.btn_addAdress)
        addAddressButton.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_adress, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(mDialogView).setTitle("Adicionar Morada")
            val mAlertDialog = mBuilder.show()

            mDialogView.findViewById<Button>(R.id.btn_dialogConfirm).setOnClickListener {
                mAlertDialog.dismiss()
                //Obter os dados do formulário
                description =
                    mAlertDialog.findViewById<EditText>(R.id.dialogDefinition)?.text.toString()
                Toast.makeText(this, description, Toast.LENGTH_LONG).show()
            }

            mDialogView.findViewById<Button>(R.id.btn_dialogCancel).setOnClickListener {
                mAlertDialog.dismiss()
                Toast.makeText(this, "Cancelou", Toast.LENGTH_LONG).show()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @ExperimentalAnimationApi
    @Composable
    fun ListAnimationComponent(addressList: List<String>) {
        val deletedAddressList = remember { mutableStateListOf<String>() }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(
                items = addressList
            ) { index, address ->
                AnimatedVisibility(
                    visible = !deletedAddressList.contains(address),
                    enter = expandVertically(),
                    exit = shrinkVertically(
                        animationSpec = tween(
                            durationMillis = 500,
                        )
                    )
                ) {
                    var openDialog by remember {
                        mutableStateOf(false) // Initially dialog is closed
                    }
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(6.dp),
                        onClick = {
                            openDialog = true
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                address, style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                ), modifier = Modifier.padding(16.dp)
                            )
                            IconButton(
                                onClick = {
                                    deletedAddressList.add(address)

                                    //TODO also delete in DB
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }

                    if (openDialog) {
                        DialogInfo(address) { openDialog = false }
                    }
                }
            }
        }
    }

    @Composable
    fun DialogInfo(address: String, onDismiss: () -> Unit) {
        val contextForToast = LocalContext.current.applicationContext

        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        text = address,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 20.sp
                        )
                    )

                    Text(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                        text = "Some bla bla",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp
                        )
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp, start = 36.dp, end = 36.dp, bottom = 8.dp),
                        onClick = {
                            onDismiss()
                            Toast.makeText(
                                contextForToast,
                                "Click: Setup Now",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        Text(
                            text = "Setup Now",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                    }

                    TextButton(
                        onClick = {
                            onDismiss()
                            Toast.makeText(
                                contextForToast,
                                "Click: I'll Do It Later",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        Text(
                            text = "I'll Do It Later",
                            color = Color(0xFF35898f),
                            style = TextStyle(
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}


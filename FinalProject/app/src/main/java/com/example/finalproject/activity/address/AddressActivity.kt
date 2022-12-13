package com.example.finalproject.activity.address

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.drawerlayout.widget.DrawerLayout
import com.example.finalproject.R
import com.example.finalproject.activity.MainActivity
import com.example.finalproject.activity.occurrence.ListNewOccurrenceActivity
import com.example.finalproject.activity.usercontrol.SettingsActivity
import com.example.finalproject.firebase.utils.AddressUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class AddressActivity : AppCompatActivity() {

    lateinit var addAddressButton: FloatingActionButton
    lateinit var description: String
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adresses)

        val addresses = this.intent.extras?.getString("favPlaces") ?: ""
        findViewById<ComposeView>(R.id.my_composable).setContent {
            ListAnimationComponent(AddressUtils.handleAddressList(addresses))
        }

        toolbar = findViewById(R.id.myToolBar2)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerAdress)
        val navView: NavigationView = findViewById(R.id.nav_view01)
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


package esprims.gi2.ma_pharmacie.presentation.main

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.ActivityMainBinding
import esprims.gi2.ma_pharmacie.presentation.alarm.AlarmActivity
import esprims.gi2.ma_pharmacie.presentation.onBoarding.dataStore
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    public lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private  val loadingDialog :LoadingDialog by lazy {
        LoadingDialog(this)
    }
    private val TAG = "Main activity"
    private val firstVisit = true
    var isFRomReminder:Boolean=false
    private val viewModel: MainActivityViewModel by viewModels()

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
       val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        navController=navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
        setContentView(binding.root)
        binding.fab.setOnClickListener {

            navController.navigate(R.id.addReminderFragment)
        }
           /*  lifecycleScope.launch(IO) {
                 val  isUserLogin=userIsLoggedIn()

                withContext(Main)
                {
                  if(isUserLogin){

                    Toast.makeText(this@MainActivity,"boubumbura",Toast.LENGTH_SHORT).show()
                    val inflater = navHostFragment.navController.navInflater
                    val graph = inflater.inflate(R.navigation.pharmacy_nav_graph)
                    graph.setStartDestination(R.id.reminderFragment)
                    navHostFragment.navController.graph = graph
                      setContentView(binding.root)


                  }
                    else{

                       setContentView(binding.root)
                  }
            }


        }*/

    }

    private fun navigateToReminderScreen() {
        val hostNavFragment =
            supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        navController = hostNavFragment.findNavController()
        navController.navigate(R.id.reminderFragment)

    }

     suspend private fun userIsLoggedIn(): Boolean {


        val jwtKey = stringPreferencesKey("jwt")

          val preferences= dataStore.data.first()
         if(!preferences[jwtKey].isNullOrEmpty()){

             return true
         }

        return false

    }

    /*override fun onResume() {
        super.onResume()
        if (viewModel.navigateToLogin) {
            val hostNavFragment =
                supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            navController = hostNavFragment.findNavController()
            navController.navigate(R.id.loginFragment)

        }

    }*/


    override fun onSupportNavigateUp(): Boolean {
        navController.graph.setStartDestination(R.id.loginFragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()

    }


    private fun showCalendar() {
        val myCalendar = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setTheme(R.style.MyThemeOverlay_App_DatePicker)
            .build()
        myCalendar.show(supportFragmentManager, "my date range picker")

    }

    private fun showTimePcker() {
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            .show(
                supportFragmentManager, "time " +
                        "to take medicine"
            )
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view: View? = currentFocus
            if (view != null && view is EditText) {
                val r = Rect()
                view.getGlobalVisibleRect(r)
                val rawX = ev.rawX.toInt()
                val rawY = ev.rawY.toInt()
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private fun blockDrawerBeforeMenuScreen() {
     //   binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(esprims.gi2.ma_pharmacie.R.menu.menu_top_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapsTopBar-> {
                navigateToMapsFragment()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun deleteJwtFromLocalStorage() {

        val key="jwt"
        val clearJwt=""
        val jwtKey = stringPreferencesKey(key)
        lifecycleScope.launch(IO) {


                       dataStore.edit { settings ->
                           settings[jwtKey]=clearJwt
                       }

                        withContext(Main) {
                            loadingDialog.hideDialog()
                            navigateTologinScreen()
                            showLogoutToast()
                        }


                }
        }





    private fun navigateTologinScreen() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.loginFragment)
    }
    private fun navigateToMapsFragment(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.pharmacyFragment)
    }

    private fun showLogoutToast() {
        val disconnectMessage="Vous êtes maintenant déconnecté"
        Toasty.success(this, disconnectMessage, Toast.LENGTH_SHORT, true).show();

    }


    override fun onPause() {
        super.onPause()

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }




}

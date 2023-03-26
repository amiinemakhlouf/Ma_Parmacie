package esprims.gi2.ma_pharmacie.presentation.main

import android.annotation.SuppressLint
import android.content.Context

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.ActivityMainBinding
import esprims.gi2.ma_pharmacie.presentation.alarm.AlarmActivity
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    public lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private  val TAG="Main activity"
    private  val firstVisit=true


    private val viewModel: MainActivityViewModel by viewModels()
    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment

        val graph = navHostFragment.navController.graph
        graph.setStartDestination(R.id.loginFragment)
        navHostFragment.navController.graph=graph
        blockDrawerBeforeMenuScreen()
        val intent=Intent(this, AlarmActivity::class.java)




        Log.d(TAG,"i'm here")
        onSystemBackButtonClicked(supportFragmentManager.fragments.last())



      //  val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
       // vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))



       // ScheduleNotificationUseCase().invoke(this)


      /*  val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, AppDatabase.databaseName
        ).fallbackToDestructiveMigration().build()
        val myMedicines= mutableListOf<List<Medicine>>()
         lifecycleScope.launch(IO)
         {

            db.medicineDao().getAll().collectLatest{it->

                if (it.isNotEmpty())
                {
                    for(medicine in it )
                    {
                        Log.d("my medicine",medicine.toString())
                    }
                }
            }

         }
        startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        val t=Toast.makeText(this,"i'm amine",Toast.LENGTH_SHORT).show()
        binding.bt.setOnClickListener {
            lifecycleScope.launch(IO)
            {

                    db.medicineDao().insert(
                        Medicine(name = "nari", codabar = "tunisiei",id=5,
                        gender = Gender.AGNOSTIC, ageCategory = AgeCategory.ADULT.toString(),
                            expirationDate = 33333, additionalDescription = "hola ya matarr",
                            form = MedicineForm.CREAM, type = MedicineType.ANTIDEPRESSANT

                    )
                    )
            }

            db.runInTransaction(Runnable {

            })


            val alarmManager =getSystemService(AlarmManager::class.java)
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+(1000*10),
                PendingIntent.getActivity(
                    this@MainActivity,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

                )

            )


        }*/

        binding.bt.setOnClickListener {

           showCalendar()
            showTimePcker()

        }




        setUpDrawer()



    }
    override fun onSupportNavigateUp(): Boolean {
        navController.graph.setStartDestination(R.id.loginFragment)
        return navController.navigateUp(appBarConfiguration)
                ||super.onSupportNavigateUp()

    }
   private fun setUpDrawer()
    {
        val hostNavFragment=supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        navController=hostNavFragment.findNavController()


        binding.navigationView.setupWithNavController(navController)
        appBarConfiguration= AppBarConfiguration(
            setOf(R.id.mapsFragment,R.id.notificationFragment,R.id.checkUpFragment, R.id.notificationFragment,
                R.id.familyFragment,R.id.healthTrackFragment,R.id.reminderFragment
            )
            ,binding.drawer)
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(navController,appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)

    }

    private fun showCalendar()
    {
         val myCalendar= MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setTheme(R.style.MyThemeOverlay_App_DatePicker)
            .build()
        myCalendar.show(supportFragmentManager,"my date range picker")

    }

    private  fun showTimePcker()
    {
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            .show(supportFragmentManager,"time " +
                "to take medicine")
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




   private fun blockDrawerBeforeMenuScreen()
    {
       binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    }








}

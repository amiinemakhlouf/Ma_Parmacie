package esprims.gi2.ma_pharmacie.presentation.main
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.local.database.AppDatabase
import esprims.gi2.ma_pharmacie.data.local.entity.Medicine
import esprims.gi2.ma_pharmacie.data.local.entity.MedicineSchedule
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.AgeCategory
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.Gender
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineForm
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import esprims.gi2.ma_pharmacie.databinding.ActivityMainBinding
import esprims.gi2.ma_pharmacie.presentation.alarm.AlarmActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private  var toast:Toast?=null

    private val viewModel: MainActivityViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d("Debuggable false:  ","none")
        val intent=Intent(this, AlarmActivity::class.java)


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



        setContentView(binding.root)
        setUpDrawer()



    }
    override fun onSupportNavigateUp(): Boolean {
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
                R.id.familyFragment,R.id.healthTrackFragment
            )
            ,binding.drawer)
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(navController,appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)

    }

    private fun showCalendar()
    {
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setTheme(R.style.MyThemeOverlay_App_DatePicker)


            .build().show(supportFragmentManager,"my date range picker")

    }

    private  fun showTimePcker()
    {
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            .show(supportFragmentManager,"time " +
                "to take medicine")
    }






}

package esprims.gi2.ma_pharmacie.presentation.main
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.ActivityMainBinding
import esprims.gi2.ma_pharmacie.presentation.AlarmActivity

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
        val intent=Intent(this,AlarmActivity::class.java)
        startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        val t=Toast.makeText(this,"i'm amine",Toast.LENGTH_SHORT).show()
        binding.bt.setOnClickListener {

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

        }



        setContentView(binding.root)
        setUpDrawer()







    }





    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)||super.onSupportNavigateUp()

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



}

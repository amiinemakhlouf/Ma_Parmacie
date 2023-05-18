package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderBinding
import esprims.gi2.ma_pharmacie.presentation.login.LoginFragmentDirections
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Date
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.DaysAdapter
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder


class ReminderFragment : Fragment() , ReminderCallback {
     private  lateinit var binding:FragmentReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentReminderBinding.inflate(layoutInflater)
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        (requireActivity() as MainActivity).binding.drawer.visibility= View.VISIBLE
        enableDrawer()
        handleAppBackButton()
        onSystemBackButtonClicked(this)
        binding.addFab.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = ReminderFragmentDirections.actionReminderFragmentToAddReminderFragment()
            navHostFragment.navController.navigate(action)
        }
        val dates= listOf<Date>(
            Date("lun",22,1),
            Date("mar",22,1),
            Date("mer",22,1),
            Date("jeu",22,1),
            Date("ven",22,1),
            Date("sam",22,1),
            Date("dim",22,1),
        )
        val reminders = listOf<Reminder>(
            Reminder("Efferalgan","2*1000g","10:00 AM","Amine"),
            Reminder("Omega 3","1*1000g","11:00 AM","Beyrem"),
            Reminder("Zartan","2*1000g","5:00 PM","Dorsaf",),
            Reminder("Efferalgan","2*1000g","5:00 AM","Amine"),
            Reminder("Omega 3","1*1000g","5:00 AM","Beyrem"),
            Reminder("Efferalgan","2*1000g","8:00 AM","Amine"),
            Reminder("Omega 3","1*1000g","8:00 AM","Beyrem"),

        )

        showDaysRecyclerView(dates)
        showReminderRecyclerView( reminders)

    }
    private fun showDaysRecyclerView( dates:List<Date>)
    {
        val daysAdapter= DaysAdapter(dates)
        binding.daysRecyclerView.apply {
            layoutManager= LinearLayoutManager(requireActivity()
                ,LinearLayoutManager.HORIZONTAL,false)
            adapter=daysAdapter
        }
    }

    private  fun showReminderRecyclerView(list:List<Reminder>)
    {
        val reminderAdapter= ReminderAdapter(list,this)
        binding.reminderRecyclerView.apply {
            layoutManager=LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
            adapter=reminderAdapter

        }
    }


    fun enableDrawer(){

        (requireActivity() as MainActivity).binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    }

    override fun navigateToDetailsScreen() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ReminderFragmentDirections.actionReminderFragmentToReminderDetailsFragment()
        navHostFragment.navController.navigate(action,  )

    }

     private fun onSystemBackButtonClicked(fragment: Fragment)
    {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            }
        fragment.requireActivity().getOnBackPressedDispatcher().addCallback(fragment.requireActivity(), callback);


    }
    private fun handleAppBackButton() {

    }

}


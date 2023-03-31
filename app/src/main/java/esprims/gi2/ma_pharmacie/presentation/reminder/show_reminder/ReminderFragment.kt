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
        enableDrawer()
        onSystemBackButtonClicked(this)
        binding.addFab.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = ReminderFragmentDirections.actionReminderFragmentToAddReminderFragment()
            navHostFragment.navController.navigate(action)
        }
        val dates= listOf<Date>(
            Date("mon",22,1),
            Date("mon",22,1),
            Date("mon",22,1),
            Date("mon",22,1), Date("mon",22,1)
        )
        val reminders = listOf<Reminder>(
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),

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
        Toast.makeText(requireContext(),"tounisir",Toast.LENGTH_SHORT).show()

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

}


package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderBinding
import esprims.gi2.ma_pharmacie.presentation.login.LoginFragmentDirections
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Date
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.DaysAdapter
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.ReminderParent
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ReminderFragment : Fragment() , ReminderCallback {
     private  lateinit var binding:FragmentReminderBinding
     private  val loadingDialog :LoadingDialog by lazy {
         LoadingDialog(requireActivity())
     }
     private  val viewModel:ReminderFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentReminderBinding.inflate(layoutInflater)
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ( requireActivity() as MainActivity).binding.bottomNavView.visibility=VISIBLE
        ( requireActivity() as MainActivity).binding.fab.visibility= View.VISIBLE
        lifecycleScope.launch(Main){
            viewModel.getAllReminders(Constants.JWT)
        }
        logout()
        updateUIAfterLogout()
        handleAppBackButton()
        onSystemBackButtonClicked(this)

        val dates= listOf<Date>(
            Date("lun",22,1),
            Date("mar",22,1),
            Date("mer",22,1),
            Date("jeu",22,1),
            Date("ven",22,1),
            Date("sam",22,1),
            Date("dim",22,1),
        )
        val reminders = listOf<List<ReminderParent>>(

           listOf(
               ReminderParent(Reminder(
                   medicationName = "Zartan", dose = "2*400g",
                     reminderTime = "08:00", personName = "jhon",
                   userEmail = "amiinemakhlouf@gmail.com"
               )),
                   ReminderParent(Reminder(
                       medicationName = "Oseltamivir", dose = "2*400g",
                       reminderTime = "08:00", personName = "Joe",
                       userEmail = "amiinemakhlouf@gmail.com"
                   ),

           ) ), listOf(
                ReminderParent(Reminder(
                    medicationName = "Zartan", dose = "2*400g",
                     reminderTime = "10:00", personName = "Jhon",
                    userEmail = "amiinemakhlouf@gmail.com"
                ),

           )),
               listOf( ReminderParent(Reminder(
                    medicationName = "Oseltamivir", dose = "2*400g",
                     reminderTime = "17:00", personName = "Joe",
                   userEmail = "amiinemakhlouf@gmail.com"
                ),


                       ),
                   ReminderParent(Reminder(
                       medicationName = "Zartan", dose = "2*400g",
                        reminderTime = "17:00", personName = "Joe",
                       userEmail = "amiinemakhlouf@gmail.com"
                   ),
               )

               ))




       // showDaysRecyclerView(dates)
        showReminderRecyclerView( reminders)

    }

    private fun updateUIAfterLogout() {
        lifecycleScope.launch(Main){
            viewModel.sharedFlowOfLogout.collectLatest {uiState->

                when(uiState){
                   is UIState.Loading -> loadingDialog.showDialog()
                   is  UIState.Success -> handleSuccesLogout()
                }

            }
        }

    }

    private fun handleSuccesLogout() {
        loadingDialog.hideDialog()
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ReminderFragmentDirections.actionReminderFragmentToLoginFragment()
        navHostFragment.navController.navigate(action)
        Toasty.error(requireContext(),"Vous etes déconnecté").show()


    }

    private fun logout() {
        binding.logoutIcon.setOnClickListener {
            lifecycleScope.launch(IO)
            {
                viewModel.logout(requireContext())

            }
        }

    }
    /*private fun showDaysRecyclerView( dates:List<Date>)
    {
        val daysAdapter= DaysAdapter(dates)
        binding.daysRecyclerView.apply {
            layoutManager= LinearLayoutManager(requireActivity()
                ,LinearLayoutManager.HORIZONTAL,false)
            adapter=daysAdapter
        }
    }*/

    private  fun showReminderRecyclerView(list:List<List<Reminder>>)
    {
        val reminderParentAdapter= ReminderParentAdapter(list,this)
        binding.reminderRecyclerView.apply {
            layoutManager=LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
            adapter=reminderParentAdapter

        }
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


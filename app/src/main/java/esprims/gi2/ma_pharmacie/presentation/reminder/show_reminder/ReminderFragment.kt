package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


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

        val today=android.text.format.DateFormat.format("EEEE", System.currentTimeMillis());
        val cal = Calendar.getInstance()
        val month_date = SimpleDateFormat("MMMM")
        val month_name = month_date.format(cal.time)


        binding.TodayDay.setText(returnDays()[today]+" "+Calendar.DAY_OF_MONTH+" "+getMonths()[month_name])

        if(viewModel.isFirstStartUp){


            lifecycleScope.launch(IO){
                viewModel.getAllReminders(Constants.JWT)

            }

        lifecycleScope.launch(Main){
            viewModel.stateFlowOfReminders.collectLatest {uiState->
                when(uiState)
                {
                    is UIState.Loading ->{
                        loadingDialog.showDialog()

                    }
                    is  UIState.Error ->{
                        loadingDialog.hideDialog()
                        Toasty.error(requireContext(),"Un erreur est survenu").show()
                    }
                    is UIState.Success ->{
                        loadingDialog.hideDialog()
                        var reminders=uiState.data
                        reminders= reminders!!.filter { it.days=="full" || it.days.contains(getTodayIn3letters()[today]!!) }
                        for (reminder in reminders)
                        {
                            Log.d("Reminder","  "+reminder.days)

                        }
                        if(reminders!!.isNotEmpty()){
                        }
                        else{
                            binding.noItems.visibility= VISIBLE

                        }
                        Log.d("ReminderFragment", reminders.toString())
                         viewModel.listByTime= mutableListOf<List<Reminder>>()
                       val hash= reminders.groupBy { it.reminderTime }
                        for (reminder in hash){

                            viewModel.listByTime.add(reminder.value)

                        }
                        showReminderRecyclerView(viewModel.listByTime)

                    }
                }
            }
        }
            viewModel.isFirstStartUp=false
        }
        else{
            if(viewModel.listByTime.isNotEmpty()){
                binding.noItems.isVisible=false
                showReminderRecyclerView(viewModel.listByTime)
            }
        }

        logout()
        updateUIAfterLogout()
        handleAppBackButton()
        onSystemBackButtonClicked(this)

       // showDaysRecyclerView(dates)

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


    override fun navigateToDetailsScreen(reminder: Reminder) {

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ReminderFragmentDirections.actionReminderFragmentToReminderDetailsFragment(
            medicationName = reminder.medicationName,
            dose = reminder.dose,
            username =reminder.personName,
            days = reminder.days,
            moment = reminder.moment,
            startDate = reminder.startDate,
            endDate=reminder.endDate




        )

        navHostFragment.navController.navigate(action)
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
    fun getDate(timestamp: Long) :String {
        val calendar = Calendar.getInstance(Locale.FRENCH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy",calendar).toString()
        return date
    }

    fun returnDays(): HashMap<String, String> {


    val daysMap = HashMap<String, String>()
    daysMap["Monday"] = "Lundi"
    daysMap["Tuesday"] = "Mardi"
    daysMap["Wednesday"] = "Mercredi"
    daysMap["Thursday"] = "Jeudi"
    daysMap["Friday"] = "Vendredi"
    daysMap["Saturday"] = "Samedi"
    daysMap["Sunday"] = "Dimanche"
        return daysMap
    }

    private fun getTodayIn3letters(): HashMap<String, String> {
        val TodayIn3LetterMap = HashMap<String, String>()
        TodayIn3LetterMap["Monday"] = "lun"
        TodayIn3LetterMap["Tuesday"] = "mar"
        TodayIn3LetterMap["Wednesday"] = "mer"
        TodayIn3LetterMap["Thursday"] = "jeu"
        TodayIn3LetterMap["Friday"] = "ven"
        TodayIn3LetterMap["Saturday"] = "sam"
        TodayIn3LetterMap["Sunday"] = "dim"
        return TodayIn3LetterMap
    }

    fun getMonths(): HashMap<String, String> {

        val monthsMap = HashMap<String, String>()
    monthsMap["January"] = "Janvier"
    monthsMap["February"] = "Février"
    monthsMap["March"] = "Mars"
    monthsMap["April"] = "Avril"
    monthsMap["May"] = "Mai"
    monthsMap["June"] = "Juin"
    monthsMap["July"] = "Juillet"
    monthsMap["August"] = "Août"
    monthsMap["September"] = "Septembre"
    monthsMap["October"] = "Octobre"
    monthsMap["November"] = "Novembre"
    monthsMap["December"] = "Décembre"
        return monthsMap
    }


}


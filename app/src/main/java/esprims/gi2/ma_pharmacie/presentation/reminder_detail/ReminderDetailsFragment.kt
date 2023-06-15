package esprims.gi2.ma_pharmacie.presentation.reminder_detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminderBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder.AddReminderDaysAdapter
import esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder.AddReminderDaysAdapter.DayListener
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked
import java.util.*

class ReminderDetailsFragment : Fragment(), DayListener {

    private lateinit var binding: FragmentAddReminderBinding
    private val reminderDetailsFragmentArgs: ReminderDetailsFragmentArgs by navArgs()
    private var listOfCardViews= mutableListOf<MaterialCardView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddReminderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSystemBackButtonClicked(this)
        (requireActivity() as MainActivity).binding.fab.visibility = View.GONE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility = View.GONE
        binding.NameET.setEndIconTintList(ColorStateList.valueOf(Color.TRANSPARENT))
        binding.NameET.editText?.isFocusable = false
        binding.NameET.editText?.isClickable = false
        binding.addReminder.isClickable=false
        binding.title.setText("Les details du rappel")
        binding.addReminder.text="les heures de prise"
        binding.daysSwitch.isActivated=false
        binding.addReminder.isCheckable=false
        binding.addReminder.isFocusable=false
        binding.startDate.isClickable=false
        binding.startDateTextField.isClickable=false
        binding.endDate.isClickable=false
        binding.endDateTextField.isClickable=false
        binding.doseEt.isFocusable=false
        binding.doseEt.isClickable=false
        binding.beforeMealsRadio.isClickable=false
        binding.beforeMealsRadio.isFocusable=false
        binding.beforeSleepingRadio.isClickable=false
        binding.beforeSleepingRadio.isFocusable=false
        binding.afterMealsRadio.isClickable=false
        binding.afterMealsRadio.isFocusable=false
        binding.nevermindRadio.isClickable=false
        binding.nevermindRadio.isFocusable=false
        binding.record.isClickable=false
        binding.record.isFocusable=false
        binding.addReminder.setBackgroundColor(resources.getColor(R.color.light_green,null))
        binding.addReminder.setTextColor(resources.getColor(R.color.dark_green,null))
        binding.addReminder.strokeWidth=0
        binding.addReminder.elevation=0f
        binding.dropdown.isFocusable = false
        binding.dropdown.isClickable = false
        binding.continuerFab.visibility= GONE
        binding.editFab.visibility= VISIBLE

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setUpDaysRv()
        binding.NameETT.setText(reminderDetailsFragmentArgs.username)
        binding.selectMedication.visibility= INVISIBLE
        binding.dropdown.visibility=INVISIBLE
        binding.selectMedication1.visibility= VISIBLE
        binding.dropdown1.visibility= VISIBLE
        binding.daysSwitch.isClickable=false
        binding.dropdown1.setText(reminderDetailsFragmentArgs.medicationName!!)

        binding.dropdown1.isFocusable=false
        Log.d("ReminderDetailsFragment", reminderDetailsFragmentArgs.dose!!)
        binding.doseEt.setText(reminderDetailsFragmentArgs.dose)
        val days = reminderDetailsFragmentArgs.days
        Log.d("days", days!!)
        if (days == "full") {
            binding.daysSwitch.isChecked = true

            binding.daysRv.post {
                Log.d("ReminderDetailsFragment",binding.daysRv.adapter!!.itemCount.toString())
                for (i in 0 until binding.daysRv.adapter!!.itemCount) {
                    val cardView = binding.daysRv.getChildAt(i) as MaterialCardView
                    cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                    cardView.isCheckable=false
                    cardView.isClickable=false
                    listOfCardViews.add(cardView)
                }
            }
        } else {
            binding.daysRv.post {
            if (days.contains("lun")) {
                val cardView = binding.daysRv.getChildAt(0) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                cardView.isCheckable=false
                cardView.isClickable=false
            }
                else{
                val cardView = binding.daysRv.getChildAt(0) as MaterialCardView
                cardView.isCheckable=false
                cardView.isClickable=false

            }
            if (days.contains("mar")) {
                val cardView = binding.daysRv.getChildAt(1) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                cardView.isCheckable=false
                cardView.isClickable=false
            }
                else{
                val cardView = binding.daysRv.getChildAt(1) as MaterialCardView
                cardView.isCheckable=false
                cardView.isClickable=false
            }
            if (days.contains("mer")) {
                val cardView = binding.daysRv.getChildAt(2) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                cardView.isCheckable=false
                cardView.isClickable=false
            }else{
                val cardView = binding.daysRv.getChildAt(2) as MaterialCardView
                cardView.isCheckable=false
                cardView.isClickable=false
            }
            if (days.contains("jeu")) {
                val cardView = binding.daysRv.getChildAt(3) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                cardView.isCheckable=false
                cardView.isClickable=false
            }
                else{
                val cardView = binding.daysRv.getChildAt(3) as MaterialCardView
                cardView.isCheckable=false
                cardView.isClickable=false
            }
            if (days.contains("ven")) {
                val cardView = binding.daysRv.getChildAt(4) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                cardView.isCheckable=false
                cardView.isClickable=false
            }
                else{
                val cardView = binding.daysRv.getChildAt(4) as MaterialCardView
                cardView.isCheckable=false
                cardView.isClickable=false
            }
            if (days.contains("sam")) {
                val cardView = binding.daysRv.getChildAt(5) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                cardView.isCheckable=false
                cardView.isClickable=false
            }
                else{
                val cardView = binding.daysRv.getChildAt(5) as MaterialCardView
                cardView.isCheckable=false
                cardView.isClickable=false
            }
            if (days.contains("dim")) {
                val cardView = binding.daysRv.getChildAt(6) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
                cardView.isCheckable=false
                cardView.isClickable=false
            }
                else{
                val cardView = binding.daysRv.getChildAt(6) as MaterialCardView
                cardView.isCheckable=false
                cardView.isClickable=false
            }
        }}
        val moment=reminderDetailsFragmentArgs.moment
        if(moment==0){
            binding.beforeMealsRadio.isChecked=true
        }
        if(moment==1){
            binding.afterMealsRadio.isChecked=true
        }
        if(moment==2)
        {
            binding.beforeSleepingRadio.isChecked=true
        }
        if(moment==3){
            binding.nevermindRadio.isChecked=true
        }
        val startDayInLong=reminderDetailsFragmentArgs.startDate!!.toLong()
        val endDayInLong=reminderDetailsFragmentArgs.endDate.toLong()
        val calendar= Calendar.getInstance()
        calendar.timeInMillis=startDayInLong
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        var month=calendar.get(Calendar.MONTH)
        val year=calendar.get(Calendar.YEAR)
        month+=1
        Log.d("ReminderDetailsFragment",month.toString())
        binding.startDate.setText(day.toString()+"/"+month+"/"+year)
        calendar.timeInMillis=endDayInLong
        val day2=calendar.get(Calendar.DAY_OF_MONTH)
        var month2=calendar.get(Calendar.MONTH)
        month2+=1
        Log.d("ReminderDetailsFragemnts",month2.toString())
        val year2=calendar.get(Calendar.YEAR)
        binding.endDate.setText(day2.toString()+"/"+month2+"/"+year2)

        binding.editFab.setOnClickListener {
            Toast.makeText(requireContext(),"hechmi  wahchi",Toast.LENGTH_SHORT).show()
            it.visibility= INVISIBLE
            binding.continuerFab.visibility= VISIBLE
            binding.dropdown1.isFocusableInTouchMode=true
            binding.dropdown1.isFocusable=true
            binding.NameETT.isFocusable = true
            binding.NameETT.isFocusableInTouchMode=true
            binding.addReminder.isCheckable=true
            binding.addReminder.isClickable=true
            binding.daysSwitch.isActivated=true
            binding.addReminder.isCheckable=true
            binding.addReminder.isFocusable=true
            binding.addReminder.setBackgroundColor(Color.WHITE)
            binding.startDate.isClickable=true
            binding.startDateTextField.isClickable=true
            binding.endDate.isClickable=true
            binding.endDateTextField.isClickable=true
            binding.doseEt.isFocusable=true
            binding.doseEt.isClickable=true
            var listOfRadios= mutableListOf<RadioButton>()
            listOfRadios.add(binding.beforeMealsRadio)
            listOfRadios.add(binding.beforeSleepingRadio)
            listOfRadios.add(binding.nevermindRadio)
            listOfRadios.add(binding.afterMealsRadio)
            binding.beforeMealsRadio.isClickable=true
            binding.beforeMealsRadio.isFocusable=true
            binding.beforeSleepingRadio.isClickable=true
            binding.beforeSleepingRadio.isFocusable=true
            binding.afterMealsRadio.isClickable=true
            binding.afterMealsRadio.isFocusable=true
            binding.nevermindRadio.isClickable=true
            binding.nevermindRadio.isFocusable=true
            var checkedItem:RadioButton?=null
            for (radio in listOfRadios)
            {
                if(radio.isChecked)
                {
                    checkedItem=radio
                }

            }
            for(radio in listOfRadios)
            {
                radio.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked){
                        checkedItem?.isChecked=false
                        radio.isChecked=true
                        checkedItem=radio
                    }

                }
            }
            binding.record.isClickable=true
            binding.record.isFocusable=true

                for(cardView in listOfCardViews){

                    cardView.isClickable=true
                    cardView.isCheckable=true
                }



        }

    }

    private fun showSecondReminderTime() {
        binding.second.visibility = View.VISIBLE
        binding.secondTime.visibility = View.VISIBLE
        binding.secondTrash.visibility = View.VISIBLE
    }

    private fun setUpDaysRv() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val myDataList = listOf<String>("lun", "mar", "mer", "jeu", "ven", "sam", "dim")
        val adapter = AddReminderDaysAdapter(myDataList, this)
        binding.daysRv.layoutManager = layoutManager
        binding.daysRv.adapter = adapter
    }

    private fun showThirdReminderTime() {
        binding.third.visibility = View.VISIBLE
        binding.thirdTime.visibility = View.VISIBLE
        binding.thirdTrash.visibility = View.VISIBLE
    }

    private fun showFirstReminderTime() {
        binding.first.visibility = View.VISIBLE
        binding.firstTime.visibility = View.VISIBLE
        binding.firstTrash.visibility = View.VISIBLE
    }

    override fun onDayClick(position: Int) {
        val adapter = binding.daysRv.adapter as AddReminderDaysAdapter
        val cardView = binding.daysRv.getChildAt(position) as MaterialCardView
        cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
    }

    private fun handleSelectAllDays() {
        binding.daysSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            for (i in 0 until binding.daysRv.childCount) {
                val cardView = binding.daysRv.getChildAt(i) as MaterialCardView
                cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
            }
        })
    }
}

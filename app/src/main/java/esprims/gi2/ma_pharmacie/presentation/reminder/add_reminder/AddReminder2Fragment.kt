package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminder2Binding
import java.util.*


class AddReminder2Fragment : Fragment() ,AddReminderDaysAdapter.DayListener{

    private var actualReminderITem: Int=-1
    private var checkedRadioBt: RadioButton?=null
    private  lateinit var binding:FragmentAddReminder2Binding
    private lateinit var datePicker: MaterialDatePicker<Long>
    val selectedDays= mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddReminder2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfRAdioButtons=getRAddioButtonsChoices()
        handleMedicationsTakeRadioButtons(listOfRAdioButtons)
        handleSelectAllDays()
        handleConfirmBt()
        datePicker=buildDatePicker()
        setUpDaysRv()
        handleAddReminders(getReminderView())
        handleDeleteReminder(getReminderView())
        handleSelectDate()



        binding.endDateTextField.setOnClickListener {
            selectDate(binding.endDate)
        }
        binding.endDate.setOnClickListener {
            selectDate(binding.endDate)
        }

    }

    private fun handleDeleteReminder(listOfReminders:List<kotlin.collections.List<View>>)
    {
        for (reminder in listOfReminders)
        {
            val trashIcon=reminder[2]
            trashIcon.setOnClickListener {
                trashIcon.visibility= GONE
                val text= reminder[0]
                val time=reminder[1]
                text.visibility= GONE
                time.visibility= GONE
                var indexOfReminder=1
                for(reminder2 in listOfReminders)
                {
                    if((reminder2[0] as TextView).visibility== VISIBLE){


                    (reminder2[0] as TextView).setText(""+indexOfReminder+" Prise")
                        indexOfReminder+=1
                }
                    actualReminderITem=indexOfReminder-2

                }
            }

        }
    }

    private fun handleSelectDate() {
        binding.startDateTextField.setOnClickListener {

            selectDate(binding.startDate)
        }
        binding.endDate.setOnClickListener {
            selectDate(binding.endDate)

        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
      return  MaterialDatePicker.Builder.datePicker()
            .setTitleText("Sélectionner une date")
            .build()

    }

    private fun handleConfirmBt() {
        binding.continuer.setOnClickListener {
            saveReminder()
        }
    }

    private fun handleAddReminders(list: kotlin.collections.List<kotlin.collections.List<View>>) {
        binding.addReminder.setOnClickListener {
            if(actualReminderITem==5){
                Toasty.error(requireActivity(),"Nombre de rappels dépassés.").show()
                return@setOnClickListener
            }
            actualReminderITem += 1
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Appointment time")
                    .build()

            picker.show(requireFragmentManager(), null)
            picker.addOnPositiveButtonClickListener {

                val hour: Int = picker.hour
                val minute: Int = picker.minute

                ( list[actualReminderITem][1] as TextView).setText(
                    String.format("%02d h : %02d",hour,minute)
                )
                var reminderIndex=0
                for (reminder in list[actualReminderITem]) {
                    reminder.visibility = VISIBLE

                }
                var actualReminder=1
                for (reminder in getReminderView()){

                    if(reminder[0].visibility== VISIBLE){

                        (reminder[0] as TextView).setText(""+actualReminder+"  Prise")
                        actualReminder+=1
                    }

                }


                return@addOnPositiveButtonClickListener
            }


        }
    }

    private fun handleSelectAllDays() {
        binding.daysSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener {
                buttonView, isChecked ->

            if(!isChecked){
                for (i in 0 until binding.daysRv.childCount) {
                    val cardView = binding.daysRv.getChildAt(i) as MaterialCardView
                    cardView.setCardBackgroundColor(ColorStateList.valueOf( resources.getColor(R.color.white,null)))
                }
            }
            else{

                for (i in 0 until binding.daysRv.childCount) {
                    val cardView = binding.daysRv.getChildAt(i) as MaterialCardView
                    cardView.setCardBackgroundColor(resources.getColor(R.color.light_green,null))
                }

            }
        })
    }

    private fun getRAddioButtonsChoices():List<RadioButton> {
          return listOf(binding.beforeSleepingRadio,
              binding.beforeMealsRadio,
              binding.afterMealsRadio,
              binding.nevermindRadio
          )
    }
    private  fun handleMedicationsTakeRadioButtons(radioButtons: kotlin.collections.List<RadioButton>){

        for(radioBt in radioButtons){
            radioBt.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    checkedRadioBt?.isChecked=false
                    checkedRadioBt=radioBt
                }
            }
        }
    }

    private fun selectDate(textInput:TextInputEditText) {

        datePicker.show(requireFragmentManager(),null)


        datePicker.addOnPositiveButtonClickListener {
            val selectedDateInMillis = it // get selected date in milliseconds
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDateInMillis
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Note that month is 0-indexed
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            textInput.setText(""+dayOfMonth+"/"+month+"/"+year)


        }
    }

    private  fun setUpDaysRv(){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val myDataList= listOf<String>("lun","mar","mer","jeu","ven","sam","dim")
        val adapter = AddReminderDaysAdapter(myDataList,this)
        binding.daysRv.adapter = adapter

    }
    private fun saveReminder()
    {
        Toasty.success(requireContext(),"Enregistré avec succes").show()
    }
    private fun addTimeToTakeMedication()
    {
        if(binding.sixth.isVisible){
            Toasty.error(requireActivity(),"limite de rappels dépassés").show()
            return
        }

        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Appointment time")
                .build()
        picker.show(requireFragmentManager(),null)
        picker.addOnPositiveButtonClickListener {
            val hour: Int = picker.hour
            val minute: Int = picker.minute
            if(binding.first.visibility== GONE){
                binding.first.visibility= VISIBLE
                binding.firstTime.visibility= VISIBLE
                binding.firstTrash.visibility= VISIBLE
                var time=""+hour+"h:"+minute
                if(minute.toString().length==1){
                    time += "0"
                }
                binding.firstTime.setText(time)
                return@addOnPositiveButtonClickListener
            }
            if(binding.first.visibility== VISIBLE && binding.second.visibility== GONE)
            {
                binding.second.visibility= VISIBLE
                binding.secondTime.visibility= VISIBLE
                binding.secondTrash.visibility= VISIBLE
                var time=""+hour+"h:"+minute
                if(minute.toString().length==1){
                    time += "0"
                }
                binding.secondTime.setText(time)
                return@addOnPositiveButtonClickListener
            }
            if(binding.second.visibility== VISIBLE && binding.third.visibility== GONE){
                binding.thirdTime.visibility= VISIBLE
                binding.third.visibility= VISIBLE
                binding.thirdTrash.visibility= VISIBLE
                var time=""+hour+"h:"+minute
                if(minute.toString().length==1){
                    time += "0"
                }
                binding.thirdTime.setText(time)
                return@addOnPositiveButtonClickListener

            }
            if(binding.third.visibility== VISIBLE && binding.fourth.visibility== GONE)
            {
                binding.fourth.visibility= VISIBLE
                binding.fourthTime.visibility= VISIBLE
                binding.fourthTrash.visibility= VISIBLE
                var time=""+hour+"h:"+minute
                if(minute.toString().length==1){
                    time += "0"
                }
                binding.fourthTime.setText(time)
                return@addOnPositiveButtonClickListener

            }
            if(binding.fourth.visibility== VISIBLE && binding.fifth.visibility== GONE)
            {
                binding.fifth.visibility= VISIBLE
                binding.fifthTime.visibility= VISIBLE
                binding.fifthTrash.visibility= VISIBLE
                var time=""+hour+"h:"+minute
                if(minute.toString().length==1){
                    time += "0"
                }
                binding.fifthTime.setText(time)
                return@addOnPositiveButtonClickListener

            }
            if(binding.fifth.visibility== VISIBLE)
            {
                binding.sixth.visibility= VISIBLE
                binding.sixthTime.visibility= VISIBLE
                binding.sixthTrash.visibility= VISIBLE
                (binding.root as ScrollView).isScrollbarFadingEnabled=false
                (binding.root as ScrollView).isVerticalScrollBarEnabled = true
                var time=""+hour+"h:"+minute
                if(minute.toString().length==1){
                    time += "0"
                }
                binding.sixthTime.setText(time)
                return@addOnPositiveButtonClickListener

            }




        }

    }

    override fun onDayClick(position:Int) {

        Toast.makeText(requireActivity(),"razeaz",Toast.LENGTH_SHORT).show()
        val adapter= binding.daysRv.adapter as AddReminderDaysAdapter
        if(adapter.myDataList[position] in selectedDays){
            val cardView= binding.daysRv.getChildAt(position) as MaterialCardView
            cardView.setCardBackgroundColor(Color.WHITE)
            selectedDays.remove(adapter.myDataList[position])

        }
        else{
            val cardView= binding.daysRv.getChildAt(position) as MaterialCardView
            cardView.setCardBackgroundColor(resources.getColor(R.color.light_green,null))
            selectedDays.add(adapter.myDataList[position])

        }

    }

     private  fun getReminderView():List<List<View>>
     {
        return listOf(listOf(binding.first,binding.firstTime,binding.firstTrash),
            listOf(binding.second,binding.secondTime,binding.secondTrash),
            listOf(binding.third,binding.thirdTime,binding.thirdTrash),
            listOf(binding.fourth,binding.fourthTime,binding.fourthTrash),
            listOf(binding.fifth,binding.fifthTime,binding.fifthTrash),
            listOf(binding.sixth,binding.sixthTime,binding.sixthTrash)


        )
     }


}
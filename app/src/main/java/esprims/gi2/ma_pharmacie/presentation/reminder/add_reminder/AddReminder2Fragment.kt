package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminder2Binding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddReminder2Fragment : Fragment() ,AddReminderDaysAdapter.DayListener{

    private val speechRecognizer: SpeechRecognizer by lazy { SpeechRecognizer.createSpeechRecognizer(requireContext()) }
    private var actualReminderITem: Int=-1
    private var checkedRadioBt: RadioButton?=null
    private  lateinit var binding:FragmentAddReminder2Binding
    private lateinit var datePicker: MaterialDatePicker<Long>
    val selectedDays= mutableListOf<String>()
    var isClicked=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideAppBar(requireActivity() as MainActivity)
        binding=FragmentAddReminder2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listOfRAdioButtons=getRAddioButtonsChoices()

        handleMedicationsTakeRadioButtons(listOfRAdioButtons)
        handleSelectAllDays()
        handleConfirmBt()
        setUpDaysRv()
        handleAddReminders(getReminderView())
        handleDeleteReminder(getReminderView())
        handleSelectDate()
        binding.recordSeekBar.apply {
            updateSpeaking(true)
            updateViewColor(Color.BLACK)
            updateAmplitude(0.5f)
            updateSpeed(-0.1f)
        }
        val requestPermissionLauncher= getRequestPermissionLauncher()


        binding.recordImage.setOnLongClickListener { _ ->
            isClicked=true
          when{  ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            ) ==PackageManager.PERMISSION_GRANTED ->{
              Toast.makeText(requireActivity(),"clicked",Toast.LENGTH_SHORT).show()
              recordAudio()


          }

            shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO) ->{

            }
              else  ->{

                  requestPermissionLauncher.launch(
                      android.Manifest.permission.RECORD_AUDIO)

              }
        }
            return@setOnLongClickListener true
        }

        catchWhenUserReleaseBt()




    }



    private fun catchWhenUserReleaseBt() {


        binding.recordImage.setOnTouchListener { view, event ->


                                    speechRecognizer.stopListening()
                                  binding.recordSeekBar.visibility= INVISIBLE

                true

            }






    }

    private fun recordAudio() {
        isClicked=false
        val intent= Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
        speechRecognizer.setRecognitionListener  (object :RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {
                binding.recordSeekBar.visibility= VISIBLE
            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
            }

            override fun onResults(results: Bundle?) {
            }

            override fun onPartialResults(partialResults: Bundle?) {
                Toasty.info(requireContext(),"enregistrement a commencé").show()

            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Toasty.info(requireContext(),"enregistrement a commencé").show()

            }

        })
        speechRecognizer.startListening(intent)
    }

    private fun getRequestPermissionLauncher(): ActivityResultLauncher<String> {
        return  registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toasty.success(requireContext(),"granted Bro").show()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
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
            return@setOnClickListener
        }
        binding.startDate.setOnClickListener {
            selectDate(binding.startDate)
            return@setOnClickListener
        }
        binding.endDateTextField.setOnClickListener {
            selectDate(binding.endDate)
            return@setOnClickListener
        }
        binding.endDate.setOnClickListener {
            selectDate(binding.endDate)
            return@setOnClickListener
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
      return  MaterialDatePicker.Builder.datePicker()
            .setTitleText("Sélectionner une date")
            .build()

    }

    private fun handleConfirmBt() {
        binding.continuerFab.setOnClickListener {
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

        datePicker=buildDatePicker()
        datePicker.show(requireFragmentManager(),null)


        datePicker.addOnPositiveButtonClickListener {
            val selectedDateInMillis = it // get selected date in milliseconds
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDateInMillis
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Note that month is 0-indexed
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            if(month>9){
                textInput.setText(""+dayOfMonth+"/"+month+"/"+year)
            }
            else{
                textInput.setText(""+dayOfMonth+"/"+"0"+month+"/"+year)

            }
            val startDate=binding.startDate.editableText.toString()
            val endDate=binding.endDate.editableText.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val startDateinDateFormat = dateFormat.parse(startDate)
            val startDateInMilliseconds = startDateinDateFormat!!.time
            val endDateInDateFormat=dateFormat.parse(endDate)
            val endDateInMilleseconds=endDateInDateFormat!!.time
            if(startDateInMilliseconds>endDateInMilleseconds)
            {
                binding.dateErrorMsg.visibility= VISIBLE
            }
            else{
                binding.dateErrorMsg.visibility= INVISIBLE
            }


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
package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminderBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder
import esprims.gi2.ma_pharmacie.presentation.shared.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddReminderFragment : Fragment() ,AddReminderDaysAdapter.DayListener {

    private var endDateInMilleseconds: Long?=null
    private var startDateInMilliseconds: Long?=null
    private var selectedItem= ""
    private val loadingDialog:LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
    private var shouldIRecord = true
    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(
            requireContext()
        )
    }

    private var actualReminderITem: Int = -1
    private var checkedRadioBt: RadioButton? = null
    private lateinit var binding: FragmentAddReminderBinding
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var recorder: MediaRecorder
    private var player: MediaPlayer? = null
    private var audioFile: File? = null
    private  val viewModel :AddReminderViewModel by viewModels()
    val selectedDays = mutableListOf<String>()
    private var clickToRecord = true
   private lateinit var myDataList :List<Medication>
   private val addReminderFragmentArgs:AddReminderFragmentArgs by navArgs()

    var isClicked = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideAppBar(requireActivity() as MainActivity)
        binding = FragmentAddReminderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("my  jwt",Constants.JWT)
        try {
            val args:AddReminderFragmentArgs by navArgs()
            args.source
            val uriInStringFormat=addReminderFragmentArgs.uri
            Log.d("AddReminderFragment",uriInStringFormat.toString())
            if(uriInStringFormat!="null"){
                val uri=Uri.parse(uriInStringFormat)
                binding.uploadBackgroundImage.setImageURI(uri)
                binding.quantityTv.setText(addReminderFragmentArgs.type)
                binding.NameETT.setText((addReminderFragmentArgs.personName))
                binding.selectMedication.visibility= INVISIBLE
                binding.dropdown.visibility= INVISIBLE
                binding.selectMedication1.visibility= VISIBLE
                binding.dropdown1.setText(addReminderFragmentArgs.medicationName)
                binding.dropdown1.isClickable=false
                binding.dropdown1.isFocusable=false


            }

        }
        catch (e:Exception){

            binding.uploadBackgroundImage.setImageDrawable(requireActivity().getDrawable(R.drawable.pill))
            binding.uploadBackgroundImage.alpha=0.65f
            binding.quantityTv.setText("")

        }


        lifecycleScope.launch(IO)
        {
            viewModel.getMedications()
        }

        startDateInMilliseconds=System.currentTimeMillis()
        endDateInMilleseconds=System.currentTimeMillis()
        val calendar=Calendar.getInstance()
        calendar.timeInMillis= startDateInMilliseconds!!.toLong()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)+1
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        binding.startDate.setText(day.toString()+"/"+month+"/"+year)
        binding.endDate.setText(day.toString()+"/"+month+"/"+year)
        hideAppBarWhenViewLooseFocus()
        hidBottomNavigation()
        popBackStack()
        handleRecordImageVisibility()
        handleDeleteAudioRecord()
       recorder=instanciateRecorderBasedInVersion()
        val listOfRAdioButtons = getRAddioButtonsChoices()
        handleMedicationsTakeRadioButtons(listOfRAdioButtons)
        handleSelectAllDays()
        handleConfirmBt()
        handleUpdateUIAfterConfirmBt()
        setUpDaysRv()
        handleAddReminders(getReminderView())
        handleDeleteReminder(getReminderView())
        handleSelectDate()
        setUpRecordSeekBar()
        startAudioRecord()
        setUpMedicationDropDown()
        addImageWhenItemSelected()
        val recordAudioPermission= getRequestPermissionLauncher()
        handleRecordAudio(recordAudioPermission)


    }

    private fun addImageWhenItemSelected() {


        binding.dropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(position==viewModel.items?.lastIndex){
                Toast.makeText(requireContext(),selectedItem,Toast.LENGTH_SHORT).show()
                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
                val action = AddReminderFragmentDirections.actionAddReminderFragmentToAddMedicationFragment("dsqsq")

               navHostFragment.navController.navigate(action)
            }
            else{
                selectedItem = parent.getItemAtPosition(position) as String

                Glide.with(requireActivity()).load(viewModel.items?.get(position)?.image).placeholder(R.drawable.pill).
                into(binding.uploadBackgroundImage);


            }

        }
    }

    private fun setUpMedicationDropDown() {
        lifecycleScope.launch(Main)
        {

        viewModel._mutableStateOfMedcations.collectLatest {
            when(it)
            {
                is UIState.Loading ->{
                    loadingDialog.showDialog()
                }
                is UIState.Success ->{
                    loadingDialog.hideDialog()
                    val list= mutableListOf<String>()
                    for(element in  it.data)
                    {
                        list.add(element = element.name!!)
                    }
                    list.add("ajouter médicament")
                    viewModel.items?.add(Medication())
                    val adapter = object : ArrayAdapter<String>(requireContext(), 0, list )
                    {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.medication_drop_down_item, parent, false)
                            val item = view.findViewById<TextView>(R.id.medicationItem)
                            item.setText(list[position])
                            val divider = view.findViewById<View>(R.id.dividerForMedicationItem)

                            // Show or hide the divider based on position
                            if (position == count - 1) {
                                divider.visibility = View.GONE // Hide the divider for the last item
                            } else {
                                divider.visibility = View.VISIBLE // Show the divider for other items
                            }

                            return view
                        }


                    }

                    binding.dropdown.setAdapter(adapter)
                    binding.dropdown.setDropDownBackgroundDrawable(
                        requireActivity().getResources().getDrawable(R.color.white,null));

                }
                is UIState.Error ->{
                    loadingDialog.hideDialog()
                    Toasty.error(requireActivity(),"un erreur est survenu").show()
                }        }

        }
        }

    }

    private fun handleUpdateUIAfterConfirmBt() {
        lifecycleScope.launch(Main){
            viewModel._reminderState.collect{

                when(it){
                    is UIState.Loading-> loadingDialog.showDialog()
                    is UIState.Success -> {
                        loadingDialog.hideDialog()
                        Toasty.success(requireActivity(),"le rappel est bien enregistré",Toast.LENGTH_SHORT).show()
                        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
                        val action = AddReminderFragmentDirections.actionAddReminderFragmentToReminderFragment()
                        navHostFragment.navController.navigate(action)
                    }
                    is UIState.Error ->{
                        loadingDialog.hideDialog()
                        Toasty.error(requireContext(),"un erreur est survenu au niveau serveur",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun hideAppBarWhenViewLooseFocus() {
        binding.NameETT.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                requireContext().hideKeyboard(v)
            }
        }
        binding.doseEt.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                requireContext().hideKeyboard(v)
            }
            else{
                binding.doseEt.setText("")
            }
        }
    }

    private fun handleRecordAudio(recordAudioPermission: ActivityResultLauncher<String>) {
        binding.recordImage.setOnLongClickListener { _ ->
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    audioFile = File(requireActivity().cacheDir, "audiofe.mp3")
                    recordAudio(audioFile!!)
                    catchWhenUserReleaseBt()


                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO) -> {

                    showRecordAudioPermissionExplicationDialog()

                }
                else -> {
                    recordAudioPermission.launch(
                        android.Manifest.permission.RECORD_AUDIO
                    )

                }
            }
            return@setOnLongClickListener true
        }

    }

    private fun showRecordAudioPermissionExplicationDialog() {
        val builder = AlertDialog.Builder(requireContext(),R.style.permission_dialog)
        val packageName="esprims.gi2.ma_pharmacie"
        builder.setTitle(getString(R.string.permission_required))
        builder.setMessage(getString(R.string.microphone_permission_explanation))
        builder.setPositiveButton(getString(R.string.accept)) { dialog, which ->
            // L'utilisateur a cliqué sur le bouton "Accorder", demander la permission
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)        }
        builder.setNegativeButton(getString(R.string.deny)) { dialog, which ->
            dialog.dismiss()
            Toasty.info(requireActivity(),getString(R.string.access_to_micro_refused)).show()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun startAudioRecord() {

         binding.recordImage.setOnClickListener {
             Toast.makeText(requireActivity(),"hechmijilani",Toast.LENGTH_SHORT).show()
            if (!shouldIRecord) {
                player = MediaPlayer.create(requireContext(), audioFile!!.toUri())
                 Toast.makeText(requireActivity(),"la tunisie",Toast.LENGTH_SHORT).show()
                player?.start()
                binding.recordImage.setImageDrawable(resources.getDrawable(R.drawable.baseline_pause_24))
                binding.recordImage.setOnClickListener {
                    player?.pause()
                    binding.recordImage.setImageDrawable(context?.getDrawable(R.drawable.ic_play))

                }

                    handleStopAudioRecord()
                    shouldIRecord = false
                    binding.recordSeekBar.updateSpeaking(false)


            }


        }
    }

    private fun handleStopAudioRecord() {

        player?.setOnCompletionListener {
            player?.stop()
            binding.recordSeekBar.updateSpeaking(false)
            binding.recordImage.setImageDrawable(context?.getDrawable(R.drawable.ic_play))
            shouldIRecord = false
            startAudioRecord()


        }
    }

    private fun setUpRecordSeekBar() {
        binding.recordSeekBar.apply {
            updateSpeaking(true)
            updateViewColor(Color.BLACK)
            updateAmplitude(0.5f)
            updateSpeed(-0.1f)
        }
    }

    private fun handleDeleteAudioRecord() {
        binding.deleteAudioImage.setOnClickListener {

            it.visibility = INVISIBLE
            binding.recordSeekBar.visibility = INVISIBLE
            binding.recordImage.setImageDrawable(context?.getDrawable(R.drawable.micro))
            shouldIRecord = true

        }

    }

    private fun instanciateRecorderBasedInVersion(): MediaRecorder {

        return if (Build.VERSION.SDK_INT >= 31) {
            MediaRecorder(requireActivity())
        } else {
            MediaRecorder()
        }
    }

    private fun handleRecordImageVisibility() {
        binding.record.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {

                true -> binding.recordImage.visibility = VISIBLE
                false -> binding.recordImage.visibility = INVISIBLE
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun catchWhenUserReleaseBt() {
     binding.recordImage.setOnTouchListener { view, event ->

            try {


                if (shouldIRecord == true) {


                    if (event.action == MotionEvent.ACTION_UP ||
                        event.action == MotionEvent.ACTION_DOWN ||
                        event.action == MotionEvent.ACTION_OUTSIDE ||
                        event.action == MotionEvent.ACTION_MOVE
                    ) {
                        speechRecognizer.stopListening()
                        binding.recordSeekBar.updateSpeaking(false)
                        recorder.stop()
                        binding.recordImage.setImageDrawable(context?.getDrawable(R.drawable.ic_play))
                        binding.deleteAudioImage.visibility = VISIBLE

                        shouldIRecord = false


                    }

                    true
                }
                false


            } catch (e: Exception) {


            }
            false
        }


    }

    private fun recordAudio(file: File) {

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(file).fd)
            prepare()
            start()

        }
        binding.recordSeekBar.visibility = VISIBLE
        binding.recordSeekBar.updateSpeaking(true)


    }

    private fun getRequestPermissionLauncher(): ActivityResultLauncher<String> {
        val fileName="audiofe.mp3"
        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                audioFile = File(requireActivity().cacheDir, fileName)
                recordAudio(audioFile!!)
                catchWhenUserReleaseBt()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.explanation))
                    .setMessage(R.string.microphone_permission_explanation)
                    .show()
            }
        }
    }

    private fun handleDeleteReminder(listOfReminders: List<kotlin.collections.List<View>>) {
        for (reminder in listOfReminders) {
            val trashIcon = reminder[2]
            trashIcon.setOnClickListener {
                trashIcon.visibility = GONE
                val text = reminder[0]
                val time = reminder[1]
                text.visibility = GONE
                time.visibility = GONE
                var indexOfReminder = 1
                for (reminder2 in listOfReminders) {
                    if ((reminder2[0] as TextView).visibility == VISIBLE) {


                        (reminder2[0] as TextView).setText("" + indexOfReminder + " Prise")
                        indexOfReminder += 1
                    }
                    actualReminderITem = indexOfReminder - 2

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
        return MaterialDatePicker.Builder.datePicker()
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
            if (actualReminderITem == 5) {
                Toasty.error(requireActivity(), "Nombre de rappels dépassés.").show()
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

                (list[actualReminderITem][1] as TextView).setText(
                   // String.format("%02d h : %02d", hour, minute)
                    hour.toString()+":"+minute.toString()
                )
                var reminderIndex = 0
                for (reminder in list[actualReminderITem]) {
                    reminder.visibility = VISIBLE

                }
                var actualReminder = 1
                for (reminder in getReminderView()) {

                    if (reminder[0].visibility == VISIBLE) {

                        (reminder[0] as TextView).setText("" + actualReminder + "  Prise")
                        actualReminder += 1
                    }

                }


                return@addOnPositiveButtonClickListener
            }


        }
    }

    private fun handleSelectAllDays() {
        binding.daysSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            if (!isChecked) {
                selectedDays.clear()
                for (i in 0 until binding.daysRv.childCount) {
                    val cardView = binding.daysRv.getChildAt(i) as MaterialCardView

                    cardView.setCardBackgroundColor(
                        ColorStateList.valueOf(
                            resources.getColor(
                                R.color.white,
                                null
                            )
                        )
                    )
                }
            } else {

                selectedDays.addAll(mutableListOf("lun", "mar", "mer", "jeu", "ven", "sam", "dim"))
                Log.d("my day", selectedDays.toString())
                for (i in 0 until binding.daysRv.childCount) {
                    val cardView = binding.daysRv.getChildAt(i) as MaterialCardView
                    cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))

                }

            }
        })
    }

    private fun getRAddioButtonsChoices(): List<RadioButton> {
        return listOf(
            binding.beforeSleepingRadio,
            binding.beforeMealsRadio,
            binding.afterMealsRadio,
            binding.nevermindRadio
        )
    }

    private fun handleMedicationsTakeRadioButtons(radioButtons: kotlin.collections.List<RadioButton>) {

        for (radioBt in radioButtons) {
            radioBt.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    checkedRadioBt?.isChecked = false
                    checkedRadioBt = radioBt
                }
            }
        }
    }

    private fun selectDate(textInput: TextInputEditText) {

        datePicker = buildDatePicker()
        datePicker.show(requireFragmentManager(), null)



        datePicker.addOnPositiveButtonClickListener {
            val selectedDateInMillis = it // get selected date in milliseconds
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDateInMillis
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Note that month is 0-indexed
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            if (month > 9) {
                textInput.setText("" + dayOfMonth + "/" + month + "/" + year)
            } else {
                textInput.setText("" + dayOfMonth + "/" + "0" + month + "/" + year)

            }
            val startDate = binding.startDate.editableText.toString()
            val endDate = binding.endDate.editableText.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val startDateinDateFormat = dateFormat.parse(startDate)
             startDateInMilliseconds = startDateinDateFormat!!.time
            val endDateInDateFormat = dateFormat.parse(endDate)
             endDateInMilleseconds = endDateInDateFormat!!.time
            if (startDateInMilliseconds!! > endDateInMilleseconds!!) {
                binding.dateErrorMsg.visibility = VISIBLE
            } else {
                binding.dateErrorMsg.visibility = INVISIBLE
            }


        }

    }

    private fun setUpDaysRv() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val myDataList = listOf<String>("lun", "mar", "mer", "jeu", "ven", "sam", "dim")
        val adapter = AddReminderDaysAdapter(myDataList, this)
        binding.daysRv.adapter = adapter

    }

    private fun saveReminder() {
        var days :String=""
        if(binding.daysSwitch.isChecked)
        {
            days="full"
        }
        else{
            for(day in   selectedDays){
                days+=day
            }
        }
        var moment=0
        if(binding.beforeMealsRadio.isChecked){
            moment=0
        }
        if(binding.afterMealsRadio.isChecked){
            moment=1
        }
        if(binding.beforeSleepingRadio.isChecked){
            moment=2
        }
        if(binding.nevermindRadio.isChecked){
            moment=3
        }

        val reminder=Reminder(selectedItem,binding.doseEt.text.toString()+" pillules"
                ,binding.firstTime.text.toString(),
                binding.NameETT.text.toString(),
                startDate=startDateInMilliseconds!!.toString(),
                endDate=(endDateInMilleseconds!!+86400000-1000).toString()  ,
                moment=moment,
               userEmail = "amiinemakhlouf@gmail.com",
                days = days,
                type = binding.quantityTv.text.toString(),

            )
        //Constants.EMAIL
             Log.d("AddReminderFragment",binding.firstTime.text.toString())
            viewModel.saveReminder(reminder)


    }


    override fun onDayClick(position: Int) {

        val adapter = binding.daysRv.adapter as AddReminderDaysAdapter
        if (adapter.myDataList[position] in selectedDays) {
            val cardView = binding.daysRv.getChildAt(position) as MaterialCardView
            cardView.setCardBackgroundColor(Color.WHITE)
            selectedDays.remove(adapter.myDataList[position])

        } else {
            val cardView = binding.daysRv.getChildAt(position) as MaterialCardView
            cardView.setCardBackgroundColor(resources.getColor(R.color.day_green, null))
            selectedDays.add(adapter.myDataList[position])

        }

    }

    private fun getReminderView(): List<List<View>> {
        return listOf(
            listOf(binding.first, binding.firstTime, binding.firstTrash),
            listOf(binding.second, binding.secondTime, binding.secondTrash),
            listOf(binding.third, binding.thirdTime, binding.thirdTrash),
            listOf(binding.fourth, binding.fourthTime, binding.fourthTrash),
            listOf(binding.fifth, binding.fifthTime, binding.fifthTrash),
            listOf(binding.sixth, binding.sixthTime, binding.sixthTrash)


        )
    }

    fun saveFileToInternalStorage(fileContents: String) {
        try {
            val fileOutputStream = FileOutputStream(audioFile)
            fileOutputStream.write(fileContents.toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            Toasty.error(requireContext(), "erreur dans l'enregistrément de l'audio")
        }
    }

    private fun popBackStack() {
        binding.backButton.setOnClickListener {
            try {
                addReminderFragmentArgs.source
                val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
                val action = AddReminderFragmentDirections.actionAddReminderFragmentToMedicineFragment()
                navHostFragment.navController.navigate(action)
            }
            catch (e:java.lang.Exception){
                findNavController().popBackStack()

            }

        }
    }
    private  fun returnTODashboard()
    {
        /*val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = AddReminderFragmentDirections.action
        navHostFragment.navController.navigate(action)*/


    }

    override fun onResume() {
        super.onResume()
        loadingDialog.hideDialog()
    }
    private fun hidBottomNavigation() {
        ( requireActivity() as MainActivity).binding.bottomNavView.visibility= GONE
        ( requireActivity() as MainActivity).binding.fab.visibility= GONE

    }
}





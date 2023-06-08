package esprims.gi2.ma_pharmacie.presentation.medication

import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddMedicationBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder.AddReminderAdapter
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddMedicationFragment : Fragment(), AddReminderAdapter.OnTypeListener {
    private var resultLauncher: ActivityResultLauncher<Uri>?=null
    private var selectedForOfStockage: RadioButton?=null
    private  lateinit var binding:FragmentAddMedicationBinding
    private var checkedDaysList = mutableListOf<Int>()
    lateinit var currentPhotoPath: String
    private val viewModel:AddMedicationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        hideAppBar(requireActivity() as MainActivity)
        binding= FragmentAddMedicationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.medicationImageUri?.let {
            binding.uploadBackgroundImage.setImageURI(it)
            binding.addMedicationTV.text=""
            binding.optionnalMsg.text=""
            binding.addImageIcon.visibility=INVISIBLE

        }
        ( requireActivity() as MainActivity).binding.bottomNavView.visibility= View.GONE
        ( requireActivity() as MainActivity).binding.fab.visibility= View.GONE
        binding.backButton.setOnClickListener {
            returnToShowMedicationFragment()
        }

        binding.medicationNameETT.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                requireContext().hideKeyboard(v)
            }
        }
        binding.quantityEt.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                requireContext().hideKeyboard(v)

            }
        }
        binding.medicationDescriptionET.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                requireContext().hideKeyboard(v)
            }
        }

        binding.root.setOnClickListener {
            /*Toast.makeText(requireContext(),"bujumbura",Toast.LENGTH_SHORT).show()
            (requireActivity() as MainActivity).currentFocus?.clearFocus()
            requireActivity().hideKeyboard(it)*/
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isPictureTaked ->


            if(isPictureTaked)
            {
                binding.addMedicationTV.text=""
                binding.optionnalMsg.text=""
                binding.addImageIcon.visibility=INVISIBLE
                binding.uploadBackgroundImage.setImageURI(viewModel.medicationImageUri)

            }

        }

        selectCapsuleForFirstFragmentStartUP()
        setUpMedicationTypesRv()
        addSWhenQuantityHigherThan1()
        navigateToPreviousFragment()
        handleContinueBt()
        scanBarCode()
        selectStockageRadioButton()
        clearErrorWhenTyping()
        handleFocusOnQuantityEt()
        binding.uploadBackgroundImage.setOnClickListener {

            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CAMERA)
             ==PackageManager.PERMISSION_GRANTED
            ){
                viewModel.medicationImageUri=createPhotoUri()

                viewModel.medicationImageUri?.let {
                    dispatchTakePictureIntent(imageUri = it)

                }

            }
            else{

                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),0)

            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode ==0 && grantResults.isNotEmpty())
        {
            viewModel.medicationImageUri=createPhotoUri()

            viewModel.medicationImageUri?.let {
                dispatchTakePictureIntent(imageUri = it)

            }
            Toast.makeText(requireActivity(),"la tunsiie",Toast.LENGTH_SHORT).show()

        }
    }

    private fun handleFocusOnQuantityEt() {
        binding.quantityEt.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
            {
                if(binding.quantityEt.text.toString()=="0"){
                    binding.quantityEt.setText("")
                }
            }
            else{
                requireContext().hideKeyboard(v)
            }
        }
    }

    private fun selectCapsuleForFirstFragmentStartUP() {

            binding.quantityTv.setText("capsule")

    }

    private fun scanBarCode() {
        val barCodeLauncher=provideBarCodeLauncher()
        binding.medicationNameET.setEndIconOnClickListener{

            barCodeLauncher.launch(ScanOptions())
        }
    }

    private fun handleContinueBt() {
        binding.continuer.setOnClickListener {
            if(isValidInputs()){

            /*    val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
                val action=AddReminderFragmentDirections.actionAddReminderFragmentToAddReminder2Fragment()

                navHostFragment.navController.navigate(action)*/

                navigateToBarCodeFragment()
            }
        }
    }

    private fun navigateToBarCodeFragment() {

        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action=AddMedicationFragmentDirections.actionAddReminderFragmentToScannerFragment(viewModel.medicationImageUri.toString())

        navHostFragment.navController.navigate(action)
    }

    private fun navigateToPreviousFragment() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addSWhenQuantityHigherThan1() {






        binding.quantityEt.doOnTextChanged { number, start, before, count ->

            var quantity=binding.quantityTv.text.toString()
            if(quantity[quantity.lastIndex]=='s'){
                quantity=quantity.removeRange(quantity.lastIndex,quantity.lastIndex+1)
                binding.quantityTv.text=quantity
            }
           if(number?.trim().toString().isNotBlank()){
               Log.d("are you blank","no")
               if (number.toString().toInt()>1)
               {

                 binding.quantityTv.text=quantity+"s"

                   }



               }

           }



    }

    private fun isValidInputs(): Boolean {
        val errorMsg="Merci de remplir ce champs"
        val emptyMsg=""
        var isValid=true

        if(binding.medicationNameET.editText?.text.isNullOrBlank()){
            binding.medicationNameET.helperText=errorMsg
            isValid=false

        }
        else{
            binding.medicationNameET.helperText=emptyMsg
        }
        if(binding.medicationDoseET.editText?.text.isNullOrBlank()){
            binding.medicationDoseET.helperText=errorMsg
            isValid=false
        }
        else{
            binding.medicationDoseET.helperText=emptyMsg
        }
        return  isValid

    }

    private fun getTypesImageIllustration():List<Int>
    {
        val capsule=R.drawable.capsule
        val liquid=R.drawable.liquid
        val injection=R.drawable.injection
        val tablets=R.drawable.tablet

        return listOf(capsule,liquid,injection,tablets)
    }
    private  fun setUpMedicationTypesRv(){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val myDataList=getTypesImageIllustration()
        val adapter = AddReminderAdapter(myDataList,this)
        binding.typesRv.adapter = adapter


    }
    private fun clearErrorWhenTyping(){
        val clearMsg=""

        binding.medicationDoseET.editText?.doAfterTextChanged {

            binding.medicationDoseET.helperText=clearMsg
        }
        binding.medicationDoseET.editText?.doOnTextChanged {
                text, start, before, count ->
            binding.medicationDoseET.helperText=clearMsg
        }
        binding.medicationNameET.editText?.doOnTextChanged { text, start, before, count ->
            binding.medicationNameET.helperText=""
        }

    }


    override fun onTypeClick(position: Int) {

        for (i in 0  until binding.typesRv.adapter!!.itemCount) {

            val viewHolder = binding.typesRv.findViewHolderForAdapterPosition(i)

            if (viewHolder != null) {
                // Get the child views for the item
                val imageView = viewHolder.itemView.findViewById<View>(R.id.my_image_view) as ImageView
                if(i==position){
                    if(isTypeChecked(position)){
                        imageView.setBackgroundColor(
                            resources.getColor(
                                android.R.color.darker_gray,
                                null
                            ))
                        checkedDaysList.remove(position)
                    }
                    else{
                        imageView.setBackgroundColor(
                            resources.getColor(
                                R.color.dark_green,
                                null
                            ))
                        binding.quantityEt.setText("0")
                        when(position)
                        {
                            0 -> binding.quantityTv.setText(getString(R.string.capsule))
                            1-> binding.quantityTv.setText(getString(R.string.bottles))
                            2 ->binding.quantityTv.setText(getString(R.string.injection))
                            3 ->binding.quantityTv.setText(getString(R.string.pill))
                        }
                        val quantity=binding.quantityEt.editableText.toString()
                        if(quantity.isNotBlank()){
                            if (quantity.toInt()>1)
                            {
                                binding.quantityEt.text?.let {
                                    val quantity= binding.quantityTv.text.toString()
                                    if(quantity[quantity.lastIndex]!='s'){
                                        binding.quantityTv.setText(
                                            binding.quantityTv.text.toString()+"s"
                                        )
                                    }


                                }

                            }
                        }

                        checkedDaysList.add(position)
                    }

                }
                else{
                    imageView.setBackgroundColor(
                        resources.getColor(
                            android.R.color.white,
                            null
                        ))
                    checkedDaysList.remove(position)
                }

            }
        }








            }
    private fun selectStockageRadioButton()
    {
        val radioButtons=getStockageRAdioButtons()
        for(form in radioButtons)
        {
            form.setOnCheckedChangeListener { buttonView, isChecked ->

                if(isChecked){
                    selectedForOfStockage?.isChecked=false
                    selectedForOfStockage=form
                }

            }
        }
    }

    private fun getStockageRAdioButtons(): List<RadioButton> {

        return  listOf(
            binding.RoomTemperatureRadio,
            binding.RefrigerationRadio,
            binding.FreezingRadio,
            binding.ProtectionFromLightRadio
        )
    }

    private fun isTypeChecked(position: Int)= position in checkedDaysList
     private  fun provideBarCodeLauncher(): ActivityResultLauncher<ScanOptions>
     {
         return registerForActivityResult(ScanContract()
         ) { result: ScanIntentResult ->
             if (result.contents == null) {
                 Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
             } else {
                 if(result.contents=="6192012110632")
                 {
                     Toast.makeText(requireContext(),"this is aflomad",Toast.LENGTH_LONG).show()
                 }
                 else if (result.contents=="6192402816519"){
                     Toast.makeText(requireActivity(),
                         "opalia",
                         Toast.LENGTH_LONG).show()
                     Log.d("my result",result.contents)

                 }
             }
     }


}
    private fun dispatchTakePictureIntent(imageUri: Uri) {

        resultLauncher?.launch(imageUri)
    }

    private fun savePhotoToInternalStorage(name:String,bmp:Bitmap){
        requireActivity().openFileOutput(name+"jpg",MODE_PRIVATE).use {stream->

            if(!bmp.compress(Bitmap.CompressFormat.JPEG,95,stream)){
                Toasty.error(requireContext(),"un  erreur est survenu").show()
            }

        }
    }

    private  fun loadImageFromInternalStorage():Bitmap?{
            val files=requireActivity().filesDir.listFiles()
            var bmp:Bitmap?=null
            files?.filter { it.canRead() && it.isFile && it.name.endsWith("jpg")
            }?.map {
                val bytes=it.readBytes()
                 bmp=BitmapFactory.decodeByteArray(bytes,0,bytes.size)

            }

        return  bmp


    }


    private fun createPhotoUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val photoFile = File.createTempFile("IMG_$timeStamp", ".jpg", storageDir)
        return FileProvider.getUriForFile(requireContext(), "com.example.myapp.fileprovider", photoFile)
    }

    private fun returnToShowMedicationFragment()
    {
        requireActivity().supportFragmentManager.popBackStack();

    }
    private fun getRequestPermissionLauncher(): ActivityResultLauncher<String> {

        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.medicationImageUri=createPhotoUri()
                viewModel.medicationImageUri?.let {
                    dispatchTakePictureIntent(imageUri = it)

                }
                Toast.makeText(requireActivity(),"la tunsiie",Toast.LENGTH_SHORT).show()

            }

             else {
            //  requireActivity().requestPermissions()
            }
        }}}


package esprims.gi2.ma_pharmacie.presentation.medication.add_medication

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.KeyEvent.ACTION_UP
import android.view.View.INVISIBLE
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddMedicationBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater.MedicationMeantForAdapter
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater.MedicationType
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater.MedicineForm
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MeantFor
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationFormItem
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationMeantForItem
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationTypeItem
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked


class AddMedication : Fragment() {
    private  val TAG ="AddMedicine"
    private lateinit var binding:FragmentAddMedicationBinding
    private val viewModel   by  viewModels<AddMedicationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddMedicationBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val textSizeForAdapter= getDropDownTextViewSizeForEachScreen()

        binding.confirmButton.setOnClickListener {
            var areInputsValid: Boolean
            val context=requireActivity() as AppCompatActivity
             areInputsValid=isIsAllInputsValid(context)


            if (areInputsValid){
                Toast.makeText(context,"inputs are valid",Toast.LENGTH_SHORT).show()
            }
            else {

                Toast.makeText(context, "inputs are not  valid", Toast.LENGTH_SHORT).show()
                binding.rooti.setOnTouchListener { v, event ->


                    clearScreenFromErrorState()

                    return@setOnTouchListener true
                }


            }
        }

        showMedicationFormInDropDown()
        showMedicationTypeInDropDown(textSizeForAdapter)
        onSystemBackButtonClicked(this)
        handleFocusStateFormAutoCompleteTextView(binding.medicationForm)
        handleFocusStateFormAutoCompleteTextView(binding.medicationType)
        handleFocusOnEditText(binding.medicationNameET)
        handleFocusOnEditText(binding.addDescriptionEt1)
        getMedicationExpirationDate()
        showMeantForDropDown()
        var open =false



    }

    private fun clearScreenFromErrorState() {
        clearErrorState(binding.medicationEndDateLayout)
        clearErrorState(binding.medicationType)
        clearErrorState(binding.medicationForm)
        clearErrorState(binding.medicationName)
        clearErrorState(binding.addDescriptionEt)
        clearErrorState(binding.meantFor)

    }

    private fun clearErrorState(textInputLayout: TextInputLayout)
    {
        if( textInputLayout.id ==R.id.add_descriptionEt){
            binding.descriptionErrorMessage.visibility= INVISIBLE
            binding.addDescriptionEt.error=null
            textInputLayout.isErrorEnabled = false

            return
        }
        textInputLayout.error = null
        textInputLayout.helperText = null
        textInputLayout.isErrorEnabled = false


    }




    private fun  getMedicationFormsFromResource() :MutableList<MedicationFormItem>
    {
        val medicineForms= mutableListOf<MedicationFormItem>()
        medicineForms.add(MedicationFormItem(getString(R.string.pill_form),R.drawable.pills))
        medicineForms.add(MedicationFormItem(getString(R.string.liquid_form),R.drawable.liquid))
        medicineForms.add(MedicationFormItem(getString(R.string.cream_form),R.drawable.cream))
        medicineForms.add(MedicationFormItem(getString(R.string.injection_form),R.drawable.injection))

        return medicineForms

    }

    private fun showMedicationFormInDropDown() {

        val medicationForms = getMedicationFormsFromResource()
        val adapter = MedicineForm(requireContext(), medicationForms)
        binding.medicineFormDropDown.setAdapter(adapter)
        binding.medicineFormDropDown.setOnItemClickListener { parent, view, position, id ->
             //return cursor to first position
            binding.medicineFormDropDown.setSelection(0)


        }



    }

    private  fun getMedicationTypesFromResource():List<MedicationTypeItem>
    {
        return mutableListOf<MedicationTypeItem>(
        MedicationTypeItem(getString(R.string.analgesics),getString(R.string.analgesics_explanation)),
        MedicationTypeItem(getString(R.string.antibiotics),getString(R.string.antibiotics_explanation)),
        MedicationTypeItem(getString(R.string.hormones),getString(R.string.hormones_explation)),
        MedicationTypeItem(getString(R.string.antivirals),getString(R.string.antivirals_explanation))
        )



    }
    private fun showMedicationTypeInDropDown( textSize:Int)
    {
         val medicationTypes= getMedicationTypesFromResource()

        val adapter= MedicationType(requireContext(),medicationTypes,textSize)

        binding.medicineTypeDropDown.setAdapter(adapter)


    }
    private fun showMeantForDropDown()
    {
       val list= getMedicationMeantForFromResource()
       val adapter =MedicationMeantForAdapter(requireContext(),list)
           binding.meantForET.setAdapter(adapter)


    }

    private  fun getMedicationMeantForFromResource()= mutableListOf<MedicationMeantForItem>(
        MedicationMeantForItem(MeantFor.Bébés,R.drawable.baby_boy),
        MedicationMeantForItem(MeantFor.Enfants,R.drawable.children),
        MedicationMeantForItem(MeantFor.Adultes,R.drawable.man)
    )


    private  fun handleFocusStateFormAutoCompleteTextView(textInputLAyout: TextInputLayout) {


        /*autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.requestFocus()

        }*/

         var isOpen=false
        textInputLAyout.editText?.setOnTouchListener { a,b->
             isOpen=(!isOpen)
               if(isOpen){
                   textInputLAyout.hint=""

               }

            return@setOnTouchListener true



        }

    }

    private  fun handleFocusOnEditText(editText: TextInputEditText)
    {
        val parentActivity=requireActivity() as MainActivity

        editText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus.not())          parentActivity.hideKeyboard(v)

        }
    }
    private  fun handleFocusStateTypeAutoCompleteTextView(autoCompleteTextView: AutoCompleteTextView) {


        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.requestFocus()

        }


        autoCompleteTextView.setOnDismissListener {
            autoCompleteTextView.clearFocus()
            val parentActivity=requireActivity() as MainActivity
            parentActivity.hideKeyboard(autoCompleteTextView)


        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getMedicationExpirationDate()
    {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        var currentDAte:Long
        var datePicker:MaterialDatePicker<Long>

        binding.medicationEndDate.setOnTouchListener { v, event ->

            if(event.action==ACTION_UP)
            {
                datePicker= MaterialDatePicker.Builder.datePicker()
                    .setTheme(R.style.MyThemeOverlay_App_DatePicker)
                    .setCalendarConstraints(constraintsBuilder.build())
                    .setTitleText("Select dates")
                    .build()
                datePicker.show(requireActivity().supportFragmentManager,TAG)

                datePicker.addOnPositiveButtonClickListener {
                    datePicker.selection?.let {

                        val currentDateInStringFormat= convertLongToTime(it)
                        binding.medicationEndDate.setText(currentDateInStringFormat)
                        binding.medicationEndDateLayout.isErrorEnabled=false

                    }



                }



            }
            return@setOnTouchListener true

        }




        }

    fun isIsAllInputsValid(context:AppCompatActivity) :Boolean
    {
        var areInputsValid=true
        if(!AddMedicationValidator.isTextInputValid(binding.medicationForm,context)) areInputsValid=false
        if(!AddMedicationValidator.isTextInputValid(binding.medicationName,requireActivity()  as AppCompatActivity)) areInputsValid=false
        if(!AddMedicationValidator.isTextInputValid(binding.medicationEndDateLayout,requireActivity()  as AppCompatActivity)) areInputsValid=false
        if(!AddMedicationValidator.isTextInputValid(binding.medicationType,requireActivity()  as AppCompatActivity)) areInputsValid=false
        if(!AddMedicationValidator.isTextInputValid(binding.meantFor,requireActivity()  as AppCompatActivity)) areInputsValid=false
        if(!AddMedicationValidator.isTextInputValid(binding.addDescriptionEt,requireActivity()  as AppCompatActivity)) areInputsValid=false

        return  areInputsValid

    }


    fun getDropDownTextViewSizeForEachScreen():Int{

        val  baseHeight=771
        val baseTextSize=21
        Log.d(TAG,getDeviceHeightInDp(requireContext()).toString())
        val actualDeviceScreenHeight= getDeviceHeightInDp(requireContext())

        return  (actualDeviceScreenHeight *baseTextSize /baseHeight).toInt()



    }




    }








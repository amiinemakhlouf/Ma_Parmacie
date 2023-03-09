package esprims.gi2.ma_pharmacie.presentation.medication.add_medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddMedicationBinding
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater.MedicationType
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater.MedicineForm
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationFormItem
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationTypeItem
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackClicked


class AddMedication : Fragment() {
    private  val TAG ="AddMedicine"
    private lateinit var binding:FragmentAddMedicationBinding

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

        showMedicationFormInDropDown()
        showMedicationTypeInDropDown()
        onSystemBackClicked(this)
        binding.medicationEndDAte.setOnClickListener{

            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select dates")
                .setTheme(R.style.date_picker)
                .build().show(requireActivity().supportFragmentManager,TAG)

        }

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
        binding.medicineFormDropDown.setOnItemClickListener {
                parent, view, position, id ->
            binding.medicineFormDropDown.focusable = View.NOT_FOCUSABLE
            binding.medicationForm.focusable=View.NOT_FOCUSABLE
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
    private fun showMedicationTypeInDropDown()
    {
         val medicationTypes= getMedicationTypesFromResource()

        val adapter= MedicationType(requireContext(),medicationTypes)
       binding.medicineTypeDropDown.setAdapter(adapter)

    }

    private  fun getMedicationMeantForFromResource(){


    }






}
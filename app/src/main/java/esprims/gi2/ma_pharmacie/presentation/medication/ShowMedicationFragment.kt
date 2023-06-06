package esprims.gi2.ma_pharmacie.presentation.medication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medicine
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.Gender
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineForm
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import esprims.gi2.ma_pharmacie.databinding.FragmentShowMedicationBinding
import esprims.gi2.ma_pharmacie.presentation.login.LoginFragmentDirections
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.adapters.MedicationAdapter
import esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder.AddReminderDaysAdapter
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.ReminderParentAdapter


class ShowMedicationFragment : Fragment() {
    private val binding  :FragmentShowMedicationBinding by lazy {
        FragmentShowMedicationBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(( requireActivity() as MainActivity).binding.bottomNavView.visibility!=VISIBLE){
            ( requireActivity() as MainActivity).binding.fab.visibility= VISIBLE
            (requireActivity() as MainActivity).binding.bottomNavView.visibility=VISIBLE
        }
        binding.fab1.setOnClickListener{
            navigateToAddMedicationReminder()

        }

        setUpMedicationRecyclerView()


    }

    private fun setUpMedicationRecyclerView() {

        val myDataList = listOf<Medicine>(Medicine
            (0,"","",Gender.AGNOSTIC,"",MedicineForm.CREAM,MedicineType.ANTIBIOTIC,7,""),
            Medicine(0,"","",Gender.AGNOSTIC,"",MedicineForm.CREAM,MedicineType.ANTIBIOTIC,7,""),
            Medicine(0,"","",Gender.AGNOSTIC,"",MedicineForm.CREAM,MedicineType.ANTIBIOTIC,7,""),
            Medicine(0,"","",Gender.AGNOSTIC,"",MedicineForm.CREAM,MedicineType.ANTIBIOTIC,7,""),
            Medicine(0,"","",Gender.AGNOSTIC,"",MedicineForm.CREAM,MedicineType.ANTIBIOTIC,7,""),)

        val medicationAdapter= MedicationAdapter(myDataList)
        binding.medicationRV.apply {
            layoutManager=LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL,false)
            adapter=medicationAdapter

        }

    }

    private fun navigateToAddMedicationReminder() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ShowMedicationFragmentDirections.actionShowMedicationFragmentToAddReminderFragment()
        navHostFragment.navController.navigate(action)

    }

}
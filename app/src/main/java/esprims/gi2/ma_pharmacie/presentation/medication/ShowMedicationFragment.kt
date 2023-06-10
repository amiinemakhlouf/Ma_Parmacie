package esprims.gi2.ma_pharmacie.presentation.medication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import esprims.gi2.ma_pharmacie.databinding.FragmentShowMedicationBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.adapters.MedicationAdapter


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
        binding.fab1.setOnClickListener {

            navigateToAddMedicationReminder()

        }


        setUpMedicationRecyclerView()


    }

    private fun setUpMedicationRecyclerView() {

        val myDataList = listOf<Medication>(Medication
            (0,"Zartan",type = MedicineType.CAPSULE, unit =  "10 mg", quantity = 20f),
            Medication(0,"Lipitor","", type = MedicineType.CAPSULE, unit =  "10 mg", quantity = 20f),
            Medication(0,"Advil ","", type = MedicineType.CAPSULE, unit = "40 mg",quantity = 10f),
            Medication(0,"Zoloft", type = MedicineType.CAPSULE, unit = "80 mg",quantity = 30f),
            Medication(0,"Tylenol",type = MedicineType.CAPSULE, unit = "80 mg",quantity = 15f),)

        val medicationAdapter= MedicationAdapter(myDataList)
        binding.medicationRV.apply {
            layoutManager=LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL,false)
            adapter=medicationAdapter

        }

    }

    private fun navigateToAddMedicationReminder() {

            /*val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = ShowMedicationFragmentDirections.actionMedicineFragmentToAddMedication()
            navHostFragment.navController.navigate(action)
*/


    }

}
package esprims.gi2.ma_pharmacie.presentation.medication.show_medication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import esprims.gi2.ma_pharmacie.databinding.FragmentShowMedicationBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.adapters.MedicationAdapter
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowMedicationFragment : Fragment(),MedicationAdapter.MedicationAdapterListener {

     private val showMedicationViewModel:ShowMedicationViewModel by viewModels()
    private  val loadingDialog:LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
    private lateinit var  binding  :FragmentShowMedicationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentShowMedicationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab1.setOnClickListener {
            navigateToAddMedicationReminder()
        }

        lifecycleScope.launch(Main)
        {
            showMedicationViewModel._mutableStateOfMedication.collectLatest {

                when(it)
                {
                    is UIState.Loading ->{

                        loadingDialog.showDialog()

                    }
                    is UIState.Success ->{
                        if(it.data.isEmpty())
                        {
                            binding.noItems.visibility= VISIBLE
                            loadingDialog.hideDialog()
                        }
                        else{

                            binding.noItems.visibility= INVISIBLE
                            setUpMedicationRecyclerView(myDataList = it.data)
                            binding.medicationRV.visibility= INVISIBLE
                            lifecycleScope.launch(Main)
                            {
                                delay(1000)
                                loadingDialog.hideDialog()
                                binding.medicationRV.visibility= VISIBLE
                                setUpMedicationRecyclerView(it.data)
                            }

                        }

                    }
                }

            }
        }

        }





    private fun setUpMedicationRecyclerView(myDataList:List<Medication>) {


        val medicationAdapter= MedicationAdapter(myDataList,this)
        binding.medicationRV.apply {
            layoutManager=LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL,false)
            adapter=medicationAdapter

        }

    }

    private fun navigateToAddMedicationReminder() {

            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = ShowMedicationFragmentDirections.actionShowMedicationFragmentToAddMedicationFragment()
            navHostFragment.navController.navigate(action)



    }

    override fun onItemClick(medication: Medication) {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ShowMedicationFragmentDirections.actionShowMedicationFragmentToMedicationDetailsFragment(medication)
        navHostFragment.navController.navigate(action)
    }

    override fun onResume() {
        super.onResume()
        ( requireActivity() as MainActivity).binding.fab.visibility= VISIBLE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility=VISIBLE

    }

}
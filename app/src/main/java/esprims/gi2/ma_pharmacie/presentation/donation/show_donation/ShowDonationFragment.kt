package esprims.gi2.ma_pharmacie.presentation.donation.show_donation

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
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import esprims.gi2.ma_pharmacie.databinding.FragmentShowDonationBinding
import esprims.gi2.ma_pharmacie.presentation.donation.DonationFragmentListener
import esprims.gi2.ma_pharmacie.presentation.donation.FilterDonationFragment
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.adapters.MedicationAdapter
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ShowDonationFragment : Fragment(), DonationFragmentListener {

    private  val showDonationViewModel:ShowDonationViewModel by viewModels()

    private val  binding:FragmentShowDonationBinding    by lazy {
        FragmentShowDonationBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(IO){
            showDonationViewModel.getDonations()

        }

        lifecycleScope.launch(Main)
        {


        showDonationViewModel.mutableStateFlowOfChoice.collectLatest {
            when(it)
            {
                0 -> showDonationViewModel.getDonations()
                1 -> showDonationViewModel.getDonationByEmail(Constants.EMAIL)
            }
        }
        }
        lifecycleScope.launch(Main)
        {
            showDonationViewModel.stateflowOfDonations.collectLatest {
                when(it)
                {
                   is  UIState.Success -> {

                       if(it.data!!.isEmpty())
                       {
                           binding.noItems.visibility= INVISIBLE
                       }
                       setUpRecyclerView(it.data)
                   }

                    is UIState.Error ->{
                        Toasty.error(requireContext()," un erreur est survenu.")
                    }
                }
            }
        }
        setUpRecyclerView()
        showBottomBar()
        handleBottomSheet(
        )
        binding.addDonation.setOnClickListener {
            val navHostFragment=requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action=ShowDonationFragmentDirections.actionShowDonationFragmentToAddDonationFragment()
            navHostFragment.navController.navigate(action,)
        }

    }

    private fun handleBottomSheet() {
        binding.filter.setOnClickListener {
             val filterDonationFragment= FilterDonationFragment(this,
                 _checkedItem = showDonationViewModel.mutableStateFlowOfChoice.value)
            filterDonationFragment.show(requireActivity().supportFragmentManager,"n")
        }
    }

    private fun showBottomBar() {
        (requireActivity() as MainActivity).binding.fab.visibility=VISIBLE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility=VISIBLE

    }

    private  fun setUpRecyclerView(List:List<Donation>)
    {
        val myDataList = listOf<Medication>(Medication
            (0,"Zartan",type = MedicineType.CAPSULE.name, unit =  "10 mg", quantity = 20f),
            Medication(0,"Lipitor","", type = MedicineType.CAPSULE.name, unit =  "10 mg", quantity = 20f),
            Medication(0,"Advil ","", type = MedicineType.CAPSULE.name, unit = "40 mg",quantity = 10f),
            Medication(0,"Zoloft", type = MedicineType.CAPSULE.name, unit = "80 mg",quantity = 30f),
            Medication(0,"Tylenol",type = MedicineType.CAPSULE.name, unit = "80 mg",quantity = 15f),)
        binding.donationRv.apply {

            layoutManager=LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter=MedicationAdapter(dataset =myDataList,null)
        }
    }

    override fun passDataToFragment(checkedItem: Int) {
        lifecycleScope.launch(Main)
        {
            showDonationViewModel.mutableStateFlowOfChoice.emit(checkedItem)

        }
    }
}
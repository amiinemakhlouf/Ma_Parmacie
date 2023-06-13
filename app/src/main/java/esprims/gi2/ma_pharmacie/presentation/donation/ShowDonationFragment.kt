package esprims.gi2.ma_pharmacie.presentation.donation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.data.local.enums_helpers.MedicineType
import esprims.gi2.ma_pharmacie.databinding.FragmentShowDonationBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.ShowMedicationFragmentDirections
import esprims.gi2.ma_pharmacie.presentation.medication.adapters.MedicationAdapter


class ShowDonationFragment : Fragment(),DonationFragmentListener {

    private  var _chckedItem=0
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
        setUpRecyclerView()
        showBottomBar()
        handleBottomSheet()
        binding.addDonation.setOnClickListener {
            val navHostFragment=requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action=ShowDonationFragmentDirections.actionShowDonationFragmentToAddDonationFragment()
            navHostFragment.navController.navigate(action,)
        }

    }

    private fun handleBottomSheet() {
        binding.filter.setOnClickListener {
             val filterDonationFragment=FilterDonationFragment(this, _checkedItem = _chckedItem)
            Toast.makeText(requireContext(),"maytayah 3al kilou ken zouz",Toast.LENGTH_SHORT).show()
            filterDonationFragment.show(requireActivity().supportFragmentManager,"n")
        }
    }

    private fun showBottomBar() {
        (requireActivity() as MainActivity).binding.fab.visibility=VISIBLE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility=VISIBLE

    }

    private  fun setUpRecyclerView()
    {
        val myDataList = listOf<Medication>(Medication
            (0,"Zartan",type = MedicineType.CAPSULE.name, unit =  "10 mg", quantity = 20f),
            Medication(0,"Lipitor","", type = MedicineType.CAPSULE.name, unit =  "10 mg", quantity = 20f),
            Medication(0,"Advil ","", type = MedicineType.CAPSULE.name, unit = "40 mg",quantity = 10f),
            Medication(0,"Zoloft", type = MedicineType.CAPSULE.name, unit = "80 mg",quantity = 30f),
            Medication(0,"Tylenol",type = MedicineType.CAPSULE.name, unit = "80 mg",quantity = 15f),)
        binding.donationRv.apply {

            layoutManager=LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter=MedicationAdapter(dataset =myDataList)
        }
    }

    override fun passDataToFragment(checkedItem: Int) {
        _chckedItem=checkedItem
    }
}
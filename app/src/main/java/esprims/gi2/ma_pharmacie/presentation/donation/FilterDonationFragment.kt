package esprims.gi2.ma_pharmacie.presentation.donation

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentFilterDonationBinding


class FilterDonationFragment(
    private val donationFragmentListener: DonationFragmentListener,
    val _checkedItem:Int
) : BottomSheetDialogFragment() {
    private  val binding:FragmentFilterDonationBinding by lazy {
        FragmentFilterDonationBinding.inflate(layoutInflater)
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

        if(_checkedItem==0)
        {
            binding.allDonations.isChecked=true
        }
        if(_checkedItem==1){
            binding.otherDonations.isChecked=true
        }
        if(_checkedItem==2){
            binding.myDonations.isChecked=true
        }

        binding.root.setOnCheckedChangeListener { group, checkedId ->
            var checkedItem=0
            if(binding.allDonations.isChecked){
                checkedItem=0
            }
            if(binding.otherDonations.isChecked){
                checkedItem=1
            }
            if(binding.myDonations.isChecked){
                checkedItem=2
            }

            donationFragmentListener.passDataToFragment(checkedItem)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        this.requireView()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }



    
}

interface DonationFragmentListener{

      fun passDataToFragment( checkedItem:Int=0)
}
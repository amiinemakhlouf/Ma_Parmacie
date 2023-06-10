package esprims.gi2.ma_pharmacie.presentation.donation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentFilterDonationBinding


class FilterDonationFragment : BottomSheetDialogFragment() {
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
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    
}
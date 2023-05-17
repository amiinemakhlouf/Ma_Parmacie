package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentScannerBinding


class ScannerFragment : Fragment() {
    private val binding:FragmentScannerBinding by lazy {

        FragmentScannerBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleScanBarCode()


    }

    private fun handleScanBarCode() {
        binding.barCodeScan.setOnClickListener {


        }
    }

}
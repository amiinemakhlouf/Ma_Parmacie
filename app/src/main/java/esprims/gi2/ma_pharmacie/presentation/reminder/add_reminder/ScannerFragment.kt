package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.AskUserToAddReminderBinding
import esprims.gi2.ma_pharmacie.databinding.FragmentScannerBinding
import esprims.gi2.ma_pharmacie.databinding.SearchByCodabarDialogBinding
import esprims.gi2.ma_pharmacie.presentation.register.RegisterFragmentDirections
import esprims.gi2.ma_pharmacie.presentation.shared.FragmentSource
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ScannerFragment : Fragment() {
    private val scannerFragmentArgs :ScannerFragmentArgs by navArgs()
    private val navHostFragment: NavHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment

    }
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
        val barcodeLauncher =getScanCodabarRegisterForResult()

        handleScanBarCode(barcodeLauncher)
        val dialogBinding= AskUserToAddReminderBinding.inflate(layoutInflater)
        var dialog= MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
        binding.ignoreTv.setOnClickListener {
            dialog.show()
            dialogBinding.confirm.setOnClickListener {
                navigateToSaveReminderFragment()
            }

            dialogBinding.ignore.setOnClickListener {
                navigateToShowMedication()
            }
        }


    }

    private fun navigateToShowMedication() {
        val action = ScannerFragmentDirections.actionScannerFragmentToMedicineFragment()
        navHostFragment.navController.navigate(action)
    }

    private fun getScanCodabarRegisterForResult(): ActivityResultLauncher<ScanOptions> {

        return    registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents == null) {
                Toasty.error(requireActivity(),"Un erreur est survenu").show()
                lifecycleScope.launch(Main) {
                    delay(2000)
                }

            } else {
                Toasty.success(requireActivity(),"Codabar enregistré").show()
                lifecycleScope.launch(Main){
                    delay(2000)
                    navigateToSaveReminderFragment()
                }
            }

        }

    }

    private fun navigateToSaveReminderFragment() {
        var uri: String?=null
          scannerFragmentArgs.uri?.let {
              uri=it
         }
        val action = ScannerFragmentDirections.actionScannerFragmentToAddReminder2Fragment(FragmentSource.FROM_ADDMEDICATION.ordinal,uri)
        navHostFragment.navController.navigate(action)

    }

    private fun handleScanBarCode(barcodeLauncher:ActivityResultLauncher<ScanOptions>) {
        binding.barCodeScan.setOnClickListener {
           barcodeLauncher.launch(ScanOptions())
        }
    }

}
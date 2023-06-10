package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
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
import esprims.gi2.ma_pharmacie.presentation.shared.FragmentSource
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ScannerFragment : Fragment() {
    private lateinit var dialog: AlertDialog
    private lateinit var dialogBinding: AskUserToAddReminderBinding
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
        requireActivity().setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        val barcodeLauncher =getScanCodabarRegisterForResult()
        handleScanBarCode(barcodeLauncher)
         dialogBinding= AskUserToAddReminderBinding.inflate(layoutInflater,null,false)
         dialog= MaterialAlertDialogBuilder(requireContext(),R.style.MyDialogTheme)
            .setView(dialogBinding.root).create()
        binding.ignoreTv.setOnClickListener {
            dialog.show()
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
                lifecycleScope.launch(Main){
                delay(1500)
                Toasty.success(requireActivity(),"Codabar enregistré").show()
                dialog.show()
                    dialogBinding.confirm.setOnClickListener {
                        navigateToSaveReminderFragment()

                        dialog.dismiss()

                    }
                }
            }

        }

    }

    private fun navigateToSaveReminderFragment() {
        var uri: String?=null
          scannerFragmentArgs.uri?.let {
              uri=it
         }
        Log.d("ScannerFragment",uri.toString())
        val action = ScannerFragmentDirections.actionScannerFragmentToAddReminderFragment(FragmentSource.FROM_ADDMEDICATION.ordinal,uri)
        navHostFragment.navController.navigate(action,)

    }

    private fun handleScanBarCode(barcodeLauncher:ActivityResultLauncher<ScanOptions>) {
        binding.barCodeScan.setOnClickListener {
           barcodeLauncher.launch(ScanOptions())
        }
    }

}
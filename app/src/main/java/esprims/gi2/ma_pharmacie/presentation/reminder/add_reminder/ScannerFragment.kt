package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentScannerBinding
import esprims.gi2.ma_pharmacie.presentation.register.RegisterFragmentDirections
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        val barcodeLauncher =getScanCodabarRegisterForResult()

        handleScanBarCode(barcodeLauncher)
        binding.ignoreTv.setOnClickListener {
            navigateToSaveReminderFragment()
        }


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
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ScannerFragmentDirections.actionScannerFragmentToAddReminder2Fragment()
        navHostFragment.navController.navigate(action)

    }

    private fun handleScanBarCode(barcodeLauncher:ActivityResultLauncher<ScanOptions>) {
        binding.barCodeScan.setOnClickListener {

            barcodeLauncher.launch(ScanOptions())
        }
    }

}
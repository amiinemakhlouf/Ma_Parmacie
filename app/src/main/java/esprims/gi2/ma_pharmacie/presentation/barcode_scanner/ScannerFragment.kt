package esprims.gi2.ma_pharmacie.presentation.barcode_scanner

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.databinding.AskUserToAddReminderBinding
import esprims.gi2.ma_pharmacie.databinding.FragmentScannerBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.AddMedicationViewModel
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import esprims.gi2.ma_pharmacie.presentation.shared.FragmentSource
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


@AndroidEntryPoint
class ScannerFragment : Fragment() {

    private lateinit var dialog: AlertDialog
    private  val loadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
    private lateinit var dialogBinding: AskUserToAddReminderBinding
    private val scannerFragmentArgs: ScannerFragmentArgs by navArgs()
    private val scannerFragmentViewModel: AddMedicationViewModel by viewModels()
    private val navHostFragment: NavHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment

    }
    private val binding: FragmentScannerBinding by lazy {

        FragmentScannerBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );
        val barcodeLauncher = getScanCodabarRegisterForResult()
        handleScanBarCode(barcodeLauncher)
        dialogBinding = AskUserToAddReminderBinding.inflate(layoutInflater, null, false)
        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setView(dialogBinding.root).create()

        lifecycleScope.launch(Main)
        {
            scannerFragmentViewModel.sharedFlowOfSaveResponse.collectLatest {

                when(it)
                {
                   is  UIState.Loading ->{

                       loadingDialog.showDialog()
                    }

                    is UIState.Success ->{
                        loadingDialog.hideDialog()
                        (requireActivity() as MainActivity).binding.fab.isVisible=true
                        (requireActivity() as MainActivity).binding.bottomNavView.isVisible=true
                        val action = ScannerFragmentDirections.actionScannerFragmentToMedicineFragment()
                        navHostFragment.navController.navigate(action)

                    }

                    is UIState.Error ->{
                        loadingDialog.hideDialog()
                        Toasty.error(requireContext(),"un erreur est survenu")
                    }
                }

            }
        }

        binding.ignoreTv.setOnClickListener {
            dialog.show()
            dialogBinding.ignore.setOnClickListener {
                dialog.dismiss()
                saveMedication()
            }

        }

    }

    private fun saveMedication() {

        val medication = Medication(
            name = scannerFragmentArgs.medicationName,
            type = scannerFragmentArgs.type,
            quantity = scannerFragmentArgs.qunatity,
            additionalDescription = scannerFragmentArgs.description,
            image = scannerFragmentArgs.uri,
            state = scannerFragmentArgs.state
        )
        Log.d("ScannerFragment", "  " + scannerFragmentArgs.uri)

        val file=scannerFragmentViewModel.file
        val requestFile = RequestBody.create(MediaType.parse("image/*"), Constants.FILE)
        val imagePart = MultipartBody.Part.createFormData("image", Constants.FILE!!.name, requestFile)
        scannerFragmentViewModel.saveMedication(medication, imagePart)

        }








    private fun getScanCodabarRegisterForResult(): ActivityResultLauncher<ScanOptions> {

        return registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents == null) {
                Toasty.error(requireActivity(), "Un erreur est survenu").show()
                lifecycleScope.launch(Main) {
                    delay(2000)
                }

            } else {
                lifecycleScope.launch(Main) {
                    binding.barCodeScan.visibility= INVISIBLE
                    binding.ignoreTv.visibility=INVISIBLE
                    loadingDialog.hideDialog()
                    delay(1500)
                    Toasty.success(requireActivity(), "Codabar enregistré").show()
                    dialog.show()
                    dialogBinding.confirm.setOnClickListener {

                        navigateToSaveReminderFragment()
                        dialog.dismiss()

                    }
                    dialogBinding.ignore.setOnClickListener {
                        loadingDialog
                            .hideDialog()
                        dialog.dismiss()
                        saveMedication()
                    }
                }
            }

        }

    }

    private fun navigateToSaveReminderFragment() {
        var uri: String? = null
        scannerFragmentArgs.uri?.let {
            uri = it
        }
        val type = scannerFragmentArgs.type
        Log.d("ScannerFragment", uri.toString())
        val action = ScannerFragmentDirections.actionScannerFragmentToAddReminderFragment(
            FragmentSource.FROM_ADDMEDICATION.ordinal, uri, type = type,
            medicationName = scannerFragmentArgs.medicationName

        )
        navHostFragment.navController.navigate(action)

    }

    private fun handleScanBarCode(barcodeLauncher: ActivityResultLauncher<ScanOptions>) {
        binding.barCodeScan.setOnClickListener {
            loadingDialog.showDialog()
            barcodeLauncher.launch(ScanOptions())
        }
    }





}
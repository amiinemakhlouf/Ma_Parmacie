package esprims.gi2.ma_pharmacie.presentation.donation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.databinding.FragmentAddDonationBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddDonationFragment : Fragment() {
    private var resultLauncher: ActivityResultLauncher<Uri>? = null
    private val viewModel: AddDonationViewModel by viewModels()
    private val binding: FragmentAddDonationBinding by lazy {
        FragmentAddDonationBinding.inflate(layoutInflater)
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
        binding.continuerFab.setOnClickListener {
            if(!checkInputs())
            {
                Toasty.error(requireContext(),"eazeaz").show()
                return@setOnClickListener
            }
            Toasty.success(requireContext(),"tout est bon").show()
        }
        closeFragment()
        handleFocusSwitchingInInputs()
        hideBottomBar()
        showImagesAfterRoationChanges()
        resultLauncher = getCameraResultLauncher()
        getImagesFroUser()
        hideKeyboardWhenInputsLooseFocues()


    }

    private fun hideKeyboardWhenInputsLooseFocues() {
        binding.myroot.setOnClickListener {

            requireActivity().hideKeyboard(it)
        }
    }

    private fun getImagesFroUser() {
        binding.firstImage.setOnClickListener {
            if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                viewModel.imageUri1 = createPhotoUri()
                viewModel.imageView1 = binding.firstImageView
                viewModel.imageLauncherDispacher=0
                resultLauncher?.launch(viewModel.imageUri1)
            }
        }
        binding.secondImage.setOnClickListener {
            if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                viewModel.imageUri2 = createPhotoUri()
                viewModel.imageView2 = binding.secondImageView
                viewModel.imageLauncherDispacher=1
                resultLauncher?.launch(viewModel.imageUri2)
            }

        }
    }

    private fun showImagesAfterRoationChanges() {
        viewModel.imageUri1?.let { uri->
            binding.firstImageView.setImageURI(uri)
            binding.firstAdd.visibility= INVISIBLE
        }
        viewModel.imageUri2?.let { uri->
            binding.secondImageView.setImageURI(uri)
            binding.secondAdd.visibility= INVISIBLE
        }
    }

    private fun hideBottomBar() {
        (requireActivity() as MainActivity).binding.bottomNavView.visibility= GONE
        (requireActivity() as MainActivity).binding.fab.visibility= GONE

    }

    private fun handleFocusSwitchingInInputs() {

        binding.medicationNameETT.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus || binding.medicationNameETT.text!!.isNotEmpty()){
                binding.medicationNameET.hint=null
                return@setOnFocusChangeListener
            }

            binding.medicationNameET.hint="Ex:Zartan(400g)"
        }

        binding.quantityETT.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus || binding.quantityETT.text!!.isNotEmpty())
            {
                binding.quantityET.hint=""
                return@setOnFocusChangeListener
            }
            binding.quantityET.hint="Ex:20 pillules"
        }
        binding.disponibilityETT.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus || binding.disponibilityETT.text!!.isNotEmpty()){
                binding.disponibilityET.hint=null
                return@setOnFocusChangeListener
            }
            binding.disponibilityET.hint="Ex:Chaque jour de 18h:00 a 19h:00"
        }

    }

    private fun closeFragment() {
        binding.closeAdd.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getCameraResultLauncher(

    ): ActivityResultLauncher<Uri>? {

       return registerForActivityResult(ActivityResultContracts.TakePicture()) { isPictureTaked ->


            if (isPictureTaked) {

                if(viewModel.imageLauncherDispacher==0){
                    viewModel.imageView1?.let { imageView ->
                        viewModel.imageUri1.let { uri ->

                            imageView.setImageURI(uri)
                        }
                            binding.firstAdd.visibility=INVISIBLE


                    }

                }

                else{
                    viewModel.imageView2?.let { imageView ->
                        viewModel.imageUri2.let { uri ->

                            imageView.setImageURI(uri)
                        }
                            binding.secondAdd.visibility=INVISIBLE

                    }

                }



            }

        }
    }

    private fun createPhotoUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val photoFile = File.createTempFile("IMG_$timeStamp", ".jpg", storageDir)
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.myapp.fileprovider",
            photoFile
        )
    }

    private  fun checkInputs():Boolean
    {
        val helperTextMsg="merci de remplir ce champs"
        var isValid=true
        if(binding.medicationNameETT.text!!.isEmpty()){

            isValid=false
            binding.medicationNameET.helperText=helperTextMsg
        }
        if(binding.disponibilityETT.text!!.isEmpty())
        {
            isValid=false
            binding.disponibilityET.helperText=helperTextMsg
        }
        if(binding.quantityETT.text!!.isEmpty()){
            isValid=false
            binding.quantityET.helperText=helperTextMsg
        }
        if (binding.phoneNumber.text.isEmpty()){
             isValid=false
            binding.PhoneNumberTextLayout.helperText=helperTextMsg
        }
        return  isValid
    }

}

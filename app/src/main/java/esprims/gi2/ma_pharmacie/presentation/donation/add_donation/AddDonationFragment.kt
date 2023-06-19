package esprims.gi2.ma_pharmacie.presentation.donation.add_donation

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.databinding.FragmentAddDonationBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddDonationFragment : Fragment() {
    private var isCitySelcted: Boolean=false
    private var resultLauncher: ActivityResultLauncher<Uri>? = null
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
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
            if (checkInputs()) {
                lifecycleScope.launch(Main)
                {
                    viewModel.saveDonation(
                        Donation(
                             viewModel.selectedCity  ,
                            binding.medicationNameETT.text.toString(),
                            binding.quantityETT.text.toString(),
                            binding.disponibilityETT.text.toString(),
                            viewModel.imageUri1?.let { it1 ->
                                requireActivity().contentResolver.openInputStream(
                                    it1
                                )?.readBytes().toString()
                            },
                            viewModel.imageUri2?.let { it1 ->
                                requireActivity().contentResolver.openInputStream(
                                    it1
                                )?.readBytes().toString()
                            },
                            binding.phoneNumberEt.text.toString(),
                            isTaken = false,
                            email = "amiinemakhlouf@gmail.com"

                        )
                    )
                }

                return@setOnClickListener
            } else {
            }
        }
        closeFragment()
        handleFocusSwitchingInInputs()
        hideBottomBar()
        showImagesAfterRoationChanges()
        resultLauncher = getCameraResultLauncher()
        getImagesFroUser()
        hideKeyboardWhenInputsLooseFocues()
        setUpMedicationDropDown()
        clearErrorMsg()

        lifecycleScope.launch(Main)
        {
            viewModel.StaFlowOfAddReminder.collectLatest {
                when (it) {
                    is UIState.Loading -> {
                        loadingDialog.showDialog()
                    }
                    is UIState.Success -> {
                        loadingDialog.hideDialog()
                        val navHostFragment =
                            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
                        val action =
                            AddDonationFragmentDirections.actionAddDonationFragmentToShowDonationFragment()
                        navHostFragment.navController.navigate(action)
                    }
                    is UIState.Error -> Toasty.error(
                        requireContext(),
                        "un erreur est généré",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }

    private fun clearErrorMsg() {
        binding.medicationNameETT.doOnTextChanged { text, start, before, count ->
            binding.medicationNameET.helperText=""
        }
        binding.phoneNumberEt.doOnTextChanged { text, start, before, count ->

            binding.PhoneNumberTextLayout.helperText=""
        }
        binding.quantityETT.doOnTextChanged { text, start, before, count ->
            binding.quantityET.helperText=""
        }
        binding.disponibilityETT.doOnTextChanged { text, start, before, count ->
            binding.disponibilityET.helperText=""
        }
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
                viewModel.imageLauncherDispacher = 0
                resultLauncher?.launch(viewModel.imageUri1)
            }
        }
        binding.secondImage.setOnClickListener {
            if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                viewModel.imageUri2 = createPhotoUri()
                viewModel.imageView2 = binding.secondImageView
                viewModel.imageLauncherDispacher = 1
                resultLauncher?.launch(viewModel.imageUri2)
            }

        }
    }

    private fun showImagesAfterRoationChanges() {
        viewModel.imageUri1?.let { uri ->
            val bitmap = uriToBitmap(requireActivity().contentResolver, uri)
            binding.firstImageView.setImageBitmap(bitmap)
            binding.firstAdd.visibility = INVISIBLE
        }
        viewModel.imageUri2?.let { uri ->
            val bitmap = uriToBitmap(requireActivity().contentResolver, uri)
            binding.firstImageView.setImageBitmap(bitmap)
            binding.secondAdd.visibility = INVISIBLE
        }
    }

    private fun hideBottomBar() {
        (requireActivity() as MainActivity).binding.bottomNavView.visibility = GONE
        (requireActivity() as MainActivity).binding.fab.visibility = GONE

    }

    private fun handleFocusSwitchingInInputs() {

        binding.medicationNameETT.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus || binding.medicationNameETT.text!!.isNotEmpty()) {
                binding.medicationNameET.hint = null
                return@setOnFocusChangeListener
            }

            binding.medicationNameET.hint = "Ex:Zartan(400g)"
        }

        binding.quantityETT.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus || binding.quantityETT.text!!.isNotEmpty()) {
                binding.quantityET.hint = ""
                return@setOnFocusChangeListener
            }
            binding.quantityET.hint = "Ex:20 pillules"
        }
        binding.disponibilityETT.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus || binding.disponibilityETT.text!!.isNotEmpty()) {
                binding.disponibilityET.hint = null
                return@setOnFocusChangeListener
            }
            binding.disponibilityET.hint = "Ex:Chaque jour de 18h:00 a 19h:00"
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

                if (viewModel.imageLauncherDispacher == 0) {
                    viewModel.imageView1?.let { imageView ->
                        viewModel.imageUri1.let { uri ->

                            val bitmap = uriToBitmap(requireActivity().contentResolver, uri!!)
                            imageView.setImageBitmap(bitmap)
                        }
                        binding.firstAdd.visibility = INVISIBLE


                    }

                } else {
                    viewModel.imageView2?.let { imageView ->
                        viewModel.imageUri2.let { uri ->

                            val bitmap = uriToBitmap(requireActivity().contentResolver, uri!!)


                            imageView.setImageBitmap(bitmap)
                        }
                        binding.secondAdd.visibility = INVISIBLE

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

    private fun checkInputs(): Boolean {
        val helperTextMsg = "merci de remplir ce champs"
        var isValid = true
        if(!isCitySelcted)
        {
            isValid = false
            binding.selectCities.helperText = helperTextMsg




        }

        if (binding.medicationNameETT.text!!.isEmpty()) {


            isValid = false

            binding.medicationNameET.helperText = helperTextMsg
        }
        if (binding.disponibilityETT.text!!.isEmpty()) {
            isValid = false
            binding.disponibilityET.helperText = helperTextMsg
        }
        if (binding.quantityETT.text!!.isEmpty()) {
            isValid = false
            binding.quantityET.helperText = helperTextMsg
        }
        if (binding.phoneNumberEt.text!!.isEmpty()) {
            isValid = false
            binding.PhoneNumberTextLayout.helperText = helperTextMsg
        }

        return isValid
    }

    fun uriToBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val btm = BitmapFactory.decodeStream(inputStream)
           // getResizedBitmap(btm, 150)
            btm
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun setUpMedicationDropDown() {

        val list=getAllCities()
        val adapter = object : ArrayAdapter<String>(requireContext(), 0, list) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.medication_drop_down_item, parent, false)
                val item = view.findViewById<TextView>(R.id.medicationItem)
                item.setText(list[position])
                val divider = view.findViewById<View>(R.id.dividerForMedicationItem)

                // Show or hide the divider based on position
                if (position == count - 1) {
                    divider.visibility = View.GONE // Hide the divider for the last item
                } else {
                    divider.visibility = View.VISIBLE // Show the divider for other items
                }


                return view
            }



        }
        binding.dropdown.setAdapter(adapter)
        binding.dropdown.setDropDownBackgroundDrawable(
            requireActivity().getResources().getDrawable(R.color.white, null)
        );
        binding.dropdown.setOnItemClickListener { parent, view, position, id ->
            isCitySelcted =true
            binding.selectCities.helperText=""
            viewModel.selectedCity= list[position]
        }

    }

    private fun getAllCities(): List<String> {

        return  listOf(
            "Monastir",
            "Sousse",
            "Sfax",
        "Gabes",
            "Tunis",
            "Beja"
        )


    }

}

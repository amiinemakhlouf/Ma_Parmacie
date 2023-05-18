package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminderBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar


class AddReminderFragment : Fragment(),AddReminderAdapter.OnTypeListener {
    private  lateinit var binding:FragmentAddReminderBinding
    private var checkedDaysList = mutableListOf<Int>()
    private val viewModel:AddReminderViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        hideAppBar(requireActivity() as MainActivity)
        binding= FragmentAddReminderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.isFirstActivityLifeCycle){
            binding.quantityTv.setText("capsule")
        }
        setUpMedicationTypesRv()

        addSWhenQuantityHigherThan1()


        val barCodeLauncher=provideBarCodeLauncher()

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.continuer.setOnClickListener {
            if(isValidInputs()){

            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
             val action=AddReminderFragmentDirections.actionAddReminderFragmentToAddReminder2Fragment()

                navHostFragment.navController.navigate(action)
        }
        }
        binding.medicationNameET.setEndIconOnClickListener{

           barCodeLauncher.launch(ScanOptions())
        }
        clearErrorWhenTyping()

    }

    private fun addSWhenQuantityHigherThan1() {

        binding.quantityEt.doOnTextChanged { text, start, before, count ->
            val quantity=binding.quantityEt.editableText.toString()
           if(quantity.isNotBlank()){
               if (quantity.toInt()>1)
               {
                   binding.quantityEt.text?.let {
                       binding.quantityTv.setText(
                           binding.quantityTv.text.toString()+"s"
                       )

                   }

               }
           }
           }


    }

    private fun isValidInputs(): Boolean {
        val errorMsg="Merci de remplir ce champs"
        val emptyMsg=""
        var isValid=true

        if(binding.medicationNameET.editText?.text.isNullOrBlank()){
            binding.medicationNameET.helperText=errorMsg
            isValid=false

        }
        else{
            binding.medicationNameET.helperText=emptyMsg
        }
        if(binding.medicationDoseET.editText?.text.isNullOrBlank()){
            binding.medicationDoseET.helperText=errorMsg
            isValid=false
        }
        else{
            binding.medicationDoseET.helperText=emptyMsg
        }
        return  isValid

    }

    private fun getTypesImageIllustration():List<Int>
    {
        val capsule=R.drawable.capsule
        val liquid=R.drawable.liquid
        val injection=R.drawable.injection
        val tablets=R.drawable.tablet

        return listOf(capsule,liquid,injection,tablets)
    }
    private  fun setUpMedicationTypesRv(){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val myDataList=getTypesImageIllustration()
        val adapter = AddReminderAdapter(myDataList,this)
        binding.typesRv.adapter = adapter


    }
    private fun clearErrorWhenTyping(){
        val clearMsg=""

        binding.medicationDoseET.editText?.doOnTextChanged {
                text, start, before, count ->
            binding.medicationDoseET.helperText=clearMsg
        }
        binding.medicationDoseET.editText?.doOnTextChanged {
                text, start, before, count ->
            binding.medicationDoseET.helperText=clearMsg
        }
    }


    override fun onTypeClick(position: Int) {

        for (i in 0  until binding.typesRv.adapter!!.itemCount) {

            val viewHolder = binding.typesRv.findViewHolderForAdapterPosition(i)

            if (viewHolder != null) {
                // Get the child views for the item
                val imageView = viewHolder.itemView.findViewById<View>(R.id.my_image_view) as ImageView
                if(i==position){
                    if(isTypeChecked(position)){
                        imageView.setBackgroundColor(
                            resources.getColor(
                                android.R.color.darker_gray,
                                null
                            ))
                        checkedDaysList.remove(position)
                    }
                    else{
                        imageView.setBackgroundColor(
                            resources.getColor(
                                R.color.dark_green,
                                null
                            ))
                        when(position)
                        {
                            0 -> binding.quantityTv.setText(getString(R.string.capsule))
                            1-> binding.quantityTv.setText(getString(R.string.bottles))
                            2 ->binding.quantityTv.setText(getString(R.string.injection))
                            3 ->binding.quantityTv.setText(getString(R.string.pill))
                        }
                        val quantity=binding.quantityEt.editableText.toString()
                        if(quantity.isNotBlank()){
                            if (quantity.toInt()>1)
                            {
                                binding.quantityEt.text?.let {
                                    binding.quantityTv.setText(
                                        binding.quantityTv.text.toString()+"s"
                                    )

                                }

                            }
                        }

                        checkedDaysList.add(position)
                    }

                }
                else{
                    imageView.setBackgroundColor(
                        resources.getColor(
                            android.R.color.white,
                            null
                        ))
                    checkedDaysList.remove(position)
                }

            }
        }








            }

    private fun isTypeChecked(position: Int)= position in checkedDaysList
     private  fun provideBarCodeLauncher(): ActivityResultLauncher<ScanOptions>
     {
         return registerForActivityResult(ScanContract()
         ) { result: ScanIntentResult ->
             if (result.contents == null) {
                 Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
             } else {
                 if(result.contents=="6192012110632")
                 {
                     Toast.makeText(requireContext(),"this is aflomad",Toast.LENGTH_LONG).show()
                 }
                 else if (result.contents=="6192402816519"){
                     Toast.makeText(requireActivity(),
                         "opalia",
                         Toast.LENGTH_LONG).show()
                     Log.d("my result",result.contents)

                 }
             }
     }

}}
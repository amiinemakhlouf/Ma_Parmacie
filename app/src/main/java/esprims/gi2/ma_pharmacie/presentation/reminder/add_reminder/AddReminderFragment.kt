package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminderBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar


class AddReminderFragment : Fragment(),AddReminderAdapter.OnTypeListener {
    private  lateinit var binding:FragmentAddReminderBinding
    private var checkedDaysList = mutableListOf<Int>()
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
        setUpMedicationTypesRv()
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
        binding.medicationNameET.addOnEndIconChangedListener { textInputLayout, previousIcon ->

        }
        clearErrorWhenTyping()

    }

    private fun isValidInputs(): Boolean {
        val errorMsg="Merci de remplir ce champs"
        val emptyMsg=""
        var isValid=true
        if(binding.NameET.editText?.text.isNullOrBlank()){
            binding.NameET.helperText=errorMsg
            isValid=false
        }
        else{
            binding.NameET.helperText=emptyMsg

        }
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
        binding.NameET.editText?.doOnTextChanged { text, start, before, count ->
            binding.NameET.helperText=clearMsg
        }
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


}
package esprims.gi2.ma_pharmacie.presentation.medication.medication_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.databinding.FragmentMedicationDetailsBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.login.LoginFragmentDirections
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.medication.adapters.MedicationAdapter
import esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder.AddReminderAdapter
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MedicationDetailsFragment : Fragment(),AddReminderAdapter.OnTypeListener {
    private val fragmentMedicationDetailsBinding: FragmentMedicationDetailsBinding by lazy {
        FragmentMedicationDetailsBinding.inflate(layoutInflater)
    }
    private  val loadingDialog:LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
    private var selectedForOfStockage: RadioButton?=null

    private val medicationDetailsFragmentViewModel :MedicationDetailsViewModel by viewModels()
    private val medicationDetailsFragmentArgs: MedicationDetailsFragmentArgs by navArgs()
    private var checkedDaysList = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return fragmentMedicationDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).binding.bottomNavView.visibility= GONE
        (requireActivity() as MainActivity).binding.fab.visibility= GONE

        lifecycleScope.launch(Main){


        medicationDetailsFragmentViewModel._mutableSharedFlowOfUpdate.collectLatest {

            when(it)
            {
                is UIState.Success -> {
                     navigateToShowMedication()
                }
                is UIState.Error ->{
                    Toast.makeText(requireContext(),"hkaya houla",Toast.LENGTH_SHORT).show()

                }
            }
        } }

        medicationDetailsFragmentArgs.medication.apply {
            fragmentMedicationDetailsBinding.medicationNameETT.setText(this.name)
            fragmentMedicationDetailsBinding.quantityEt.setText(this.quantity!!.toInt().toString())
            fragmentMedicationDetailsBinding.quantityTv.setText(this.type)
            fragmentMedicationDetailsBinding.medicationDescriptionET.setText(this.additionalDescription)
            Glide.with(requireActivity()).load(medicationDetailsFragmentArgs.medication!!.image).
            into(fragmentMedicationDetailsBinding.uploadBackgroundImage);

            Glide.with(requireActivity()).load(medicationDetailsFragmentArgs.medication!!.image).
            into(fragmentMedicationDetailsBinding.uploadBackgroundImageStretch);
            Toast.makeText(requireContext(),medicationDetailsFragmentArgs.medication.state!!.toInt().toString(),Toast.LENGTH_SHORT).show()
            when (medicationDetailsFragmentArgs.medication.state)
            {
                1 -> {
                    fragmentMedicationDetailsBinding.RoomTemperatureRadio.isChecked=true
                    selectedForOfStockage=fragmentMedicationDetailsBinding.RoomTemperatureRadio
                }
                2-> {
                    fragmentMedicationDetailsBinding.FreezingRadio.isChecked=true
                    selectedForOfStockage=fragmentMedicationDetailsBinding.FreezingRadio

                }
                3->{
                    fragmentMedicationDetailsBinding.ProtectionFromLightRadio.isChecked=true
                    selectedForOfStockage=fragmentMedicationDetailsBinding.ProtectionFromLightRadio

                }
                4->{
                    fragmentMedicationDetailsBinding.RefrigerationRadio.isChecked=true
                    selectedForOfStockage=fragmentMedicationDetailsBinding.RefrigerationRadio

                }

        }

            setUpMedicationTypesRv()

        }
        fragmentMedicationDetailsBinding.editAll.setOnClickListener {

            it.visibility= INVISIBLE
            fragmentMedicationDetailsBinding.continuer.visibility= VISIBLE
            fragmentMedicationDetailsBinding.continuer.text="mise à jour"
            fragmentMedicationDetailsBinding.medicationNameETT.isFocusable=true
            fragmentMedicationDetailsBinding.medicationNameETT.isFocusableInTouchMode=true
            fragmentMedicationDetailsBinding.quantityEt.isFocusable=true
            fragmentMedicationDetailsBinding.quantityEt.isFocusableInTouchMode=true
            fragmentMedicationDetailsBinding.FreezingRadio.isClickable=true
            fragmentMedicationDetailsBinding.FreezingRadio.isFocusable=true
            fragmentMedicationDetailsBinding.RefrigerationRadio.isFocusable=true
            fragmentMedicationDetailsBinding.RefrigerationRadio.isClickable=true
            fragmentMedicationDetailsBinding.ProtectionFromLightRadio.isClickable=true
            fragmentMedicationDetailsBinding.ProtectionFromLightRadio.isFocusable=true
            fragmentMedicationDetailsBinding.RoomTemperatureRadio.isClickable=true
            fragmentMedicationDetailsBinding.RoomTemperatureRadio.isFocusable=true
            fragmentMedicationDetailsBinding.ProtectionFromLightRadio.isClickable=true
            fragmentMedicationDetailsBinding.ProtectionFromLightRadio.isFocusable=true

            fragmentMedicationDetailsBinding.typesRv1.isFocusable=true
            fragmentMedicationDetailsBinding.typesRv1.isFocusableInTouchMode=true
            fragmentMedicationDetailsBinding.typesRv1.isClickable=true
            fragmentMedicationDetailsBinding.medicationDescriptionET.isFocusable=true
            fragmentMedicationDetailsBinding.medicationDescriptionET.isFocusableInTouchMode=true
            fragmentMedicationDetailsBinding.medicationDescriptionET.isClickable=true
            fragmentMedicationDetailsBinding.medicationNameETT.isClickable=true

        }

        fragmentMedicationDetailsBinding.FreezingRadio.setOnCheckedChangeListener { buttonView, isChecked ->

        }


        fragmentMedicationDetailsBinding.continuer.setOnClickListener {


            var state=0
            if (fragmentMedicationDetailsBinding.RoomTemperatureRadio.isChecked)
            {
                state=1
            }
            if(fragmentMedicationDetailsBinding.FreezingRadio.isChecked)
            {
                state=2
            }
            if(fragmentMedicationDetailsBinding.ProtectionFromLightRadio.isChecked)
            {
                state=3
            }
            if(fragmentMedicationDetailsBinding.RefrigerationRadio.isChecked)
            {
                state=4
            }
            lifecycleScope.launch(IO)
            {
                medicationDetailsFragmentViewModel.updateMedication(
                    medicationDetailsFragmentArgs.medication.id!!,
                    Medication(
                        medicationDetailsFragmentArgs.medication.id,
                        fragmentMedicationDetailsBinding.medicationNameETT.text.toString(),
                        additionalDescription = fragmentMedicationDetailsBinding.medicationDescriptionET.text.toString(),
                        quantity = fragmentMedicationDetailsBinding.quantityEt.text.toString().toFloat(),
                        state = state,
                        unit = fragmentMedicationDetailsBinding.quantityEt.text.toString()
                    )
                )
            }


        }

        fragmentMedicationDetailsBinding.backButton.setOnClickListener {

            findNavController().popBackStack()
        }
        fragmentMedicationDetailsBinding.root1.setOnClickListener {
            fragmentMedicationDetailsBinding.root.requestFocus()
            requireActivity().hideKeyboard(it)
        }
      /*  fragmentMedicationDetailsBinding.editName.setOnClickListener {
            it.visibility=INVISIBLE
            fragmentMedicationDetailsBinding.medicationNameET.isFocusable=true
            fragmentMedicationDetailsBinding.medicationNameETT.isFocusable=true
        }
        fragmentMedicationDetailsBinding.descriptionEdit.setOnClickListener {
            it.visibility= INVISIBLE
            fragmentMedicationDetailsBinding.description.isFocusable=true
            fragmentMedicationDetailsBinding.medicationDescriptionET.isClickable=true
        }*/

        selectStockageRadioButton()
    }

    private fun navigateToShowMedication() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = MedicationDetailsFragmentDirections.actionMedicationDetailsFragmentToShowMedicationFragment()

        navHostFragment.navController.navigate(action)

    }

    private  fun setUpMedicationTypesRv(){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val myDataList=getTypesImageIllustration()
        val adapter = AddReminderAdapter(myDataList,this)
        fragmentMedicationDetailsBinding.typesRv1.adapter = adapter



    }
    private fun getTypesImageIllustration():List<Int>
    {
        val capsule=R.drawable.capsule
        val liquid=R.drawable.liquid
        val injection=R.drawable.injection
        val tablets=R.drawable.tablet

        return listOf(capsule,liquid,injection,tablets)
    }
    private fun isTypeChecked(position: Int)= position in checkedDaysList

    override fun onTypeClick(position: Int) {

        for (i in 0  until fragmentMedicationDetailsBinding.typesRv1.adapter!!.itemCount) {

            val viewHolder = fragmentMedicationDetailsBinding.typesRv1.findViewHolderForAdapterPosition(i)

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
                        fragmentMedicationDetailsBinding.quantityEt.setText("0")
                        when(position)
                        {
                            0 -> fragmentMedicationDetailsBinding.quantityTv.setText(getString(R.string.capsule))
                            1-> fragmentMedicationDetailsBinding.quantityTv.setText(getString(R.string.bottles))
                            2 ->fragmentMedicationDetailsBinding.quantityTv.setText(getString(R.string.injection))
                            3 ->fragmentMedicationDetailsBinding.quantityTv.setText(getString(R.string.pill))
                        }
                        val quantity=fragmentMedicationDetailsBinding.quantityEt.editableText.toString()
                        if(quantity.isNotBlank()){
                            if (quantity.toInt()>1)
                            {
                                fragmentMedicationDetailsBinding.quantityEt.text?.let {
                                    val quantity= fragmentMedicationDetailsBinding.quantityTv.text.toString()
                                    if(quantity[quantity.lastIndex]!='s'){
                                        fragmentMedicationDetailsBinding.quantityTv.setText(
                                            fragmentMedicationDetailsBinding.quantityTv.text.toString()+"s"
                                        )
                                    }


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
    private fun selectStockageRadioButton()
    {
        val radioButtons=getStockageRAdioButtons()
        for(form in radioButtons)
        {
            form.setOnCheckedChangeListener { buttonView, isChecked ->

                if(isChecked){
                    selectedForOfStockage?.isChecked=false
                    selectedForOfStockage=form
                }

            }
        }
    }
    private fun getStockageRAdioButtons(): List<RadioButton> {

        return  listOf(
            fragmentMedicationDetailsBinding.RoomTemperatureRadio,
            fragmentMedicationDetailsBinding.RefrigerationRadio,
            fragmentMedicationDetailsBinding.FreezingRadio,
            fragmentMedicationDetailsBinding.ProtectionFromLightRadio
        )


}}
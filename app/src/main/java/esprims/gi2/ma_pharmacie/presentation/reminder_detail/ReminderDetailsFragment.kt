package esprims.gi2.ma_pharmacie.presentation.reminder_detail

import android.os.Bundle
import android.text.InputType
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderBinding
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderDetailsBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class ReminderDetailsFragment : Fragment() {

  private  lateinit var binding: FragmentReminderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentReminderDetailsBinding.inflate(layoutInflater)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSystemBackButtonClicked(this)
        disableTopBar()
        (  requireActivity() as MainActivity).binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.backImage.setOnClickListener {
            requireActivity().onBackPressed();

        }

        binding.doseValue.inputType=InputType.TYPE_NULL
        binding.programValue.isEnabled=false
        binding.quantityValue.isEnabled=false
        binding.editDose.setOnClickListener {
            binding.doseValue.inputType=InputType.TYPE_CLASS_TEXT
            binding.doseValue.requestFocus()
            binding.doseValue.isCursorVisible=true

        }
        binding.editProgram.setOnClickListener {
            binding.programValue.isEnabled=true
            binding.programValue.requestFocus()
            binding.programValue.isCursorVisible=true
        }
        binding.modify.setOnClickListener {
           val loadingDialog= LoadingDialog(requireActivity())
            loadingDialog.showDialog()
            it.isEnabled=false
            lifecycleScope.launch(IO){
                delay(500)
                withContext(Main){
                    loadingDialog.hideDialog()
                    Toasty.success(requireActivity(),"Informations mises à jour avec succès").show()
                    it.isEnabled=true

                }
            }
        }
        /*binding.editQuantity.setOnClickListener {
            binding.quantityValue.inputType=InputType.TYPE_CLASS_TEXT
            binding.quantityValue.requestFocus()
            binding.quantityValue.isCursorVisible=true
        }*/
    }
    private fun disableTopBar()
    {
        ( requireActivity() as AppCompatActivity).supportActionBar!!.hide()
    }



}
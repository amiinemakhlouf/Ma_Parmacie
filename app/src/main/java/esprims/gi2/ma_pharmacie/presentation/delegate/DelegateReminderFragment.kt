package esprims.gi2.ma_pharmacie.presentation.delegate

import NotificationHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentDelegateReminderBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class DelegateReminderFragment : Fragment() {

    private val fragmentDelegateReminderBinding  :FragmentDelegateReminderBinding by lazy {
        FragmentDelegateReminderBinding.inflate(layoutInflater)
    }
    private  val navArgs:DelegateReminderFragmentArgs  by navArgs()
    private val viewModel:DelegateReminderViewModel by viewModels()
    private  val loadingDialog:LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return fragmentDelegateReminderBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).binding.fab.visibility= GONE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility= GONE
        val textToShare = "My own text\nhttps://stackoverflow.com/"

        lifecycleScope.launch(Main)
        {


        viewModel.stateOfCheckReminder.collectLatest {

            when(it)
            {
                is UIState.Success ->{
                    if(it.data?.statu!=0)
                    {
                        NotificationHelper.createNotification(requireActivity(),null,"d")

                    }
                }
            }
        }

        }

        updateUI()
        fragmentDelegateReminderBinding.backButton.setOnClickListener {
            (requireActivity() as MainActivity).binding.fab.visibility= VISIBLE
            (requireActivity() as MainActivity).binding.bottomNavView.visibility= VISIBLE

        }
        fragmentDelegateReminderBinding.confirmBt.setOnClickListener {

            lifecycleScope.launch()
            {
                val reminder=navArgs.reminder
                reminder.userEmail=fragmentDelegateReminderBinding.email.editableText.toString()
                Log.d("DelegateReminderFragment",reminder.userEmail!!)
                reminder.isDelegated=true
                reminder.source=Constants.userEmail
                viewModel.insertDelegate(reminder)

            }

        }

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.facebook.orca")
        fragmentDelegateReminderBinding.fb.setOnClickListener {
            requireActivity().startActivity(sendIntent)
        }

    }

    private fun updateUI() {

        lifecycleScope.launch(Main){


        viewModel.stateOfReminder.collectLatest {

            when(it)
            {
                is UIState.Loading ->{
                    loadingDialog.showDialog()
                }
                is UIState.Success ->{
                    loadingDialog.hideDialog()
                    val navHostFragment =requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment)
                    val action=DelegateReminderFragmentDirections.actionDelegateReminderFragmentToReminderFragment()
                    Toasty.success(requireContext(),"Demande de délégation envoyé avec success")
                    navHostFragment!!.findNavController().navigate(action)

                    withContext(IO)
                    {
                        while(true){

                            viewModel.getReminderById(it.data!!.id!!)
                           delay(100000)

                        }
                    }

                }
                is UIState.Error ->{
                    Toasty.error(requireContext(),"Utilisateur n'existe pas.")
                }
            }
        }

        }
    }


}
package esprims.gi2.ma_pharmacie.presentation.otp_confirmation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentEmailOtpBinding
import esprims.gi2.ma_pharmacie.requestModel.ConfirmRequestModel
import esprims.gi2.ma_pharmacie.requestModel.RegisterRequestModel
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.useCase.saveJwtLocally
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ConfirmEmailOtpFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var binding: FragmentEmailOtpBinding
    private val viewModel: ConfirmEmailOtpViewModel by viewModels()
    private val args: ConfirmEmailOtpFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailOtpBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireActivity() as MainActivity)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        returnToLoginScreen()
        handleConfirmBt()
        updateUIForConfirmEmailFlow()
        resendCode()
        updateUiAfterResendCode()


    }

    private fun updateUiAfterResendCode() {
        lifecycleScope.launch(Main){

        viewModel._resendCodeStateFlow.collectLatest {uiState ->

        when (uiState)
        {
            is UIState.Loading ->{
                loadingDialog.showDialog()
            }
            is UIState.Success  ->{
                Toasty.success(requireActivity(),"code renvoyé").show()
            }
            else ->loadingDialog.hideDialog()


        }

        }
        }

    }

    private fun updateUIForConfirmEmailFlow() {
        lifecycleScope.launch(Main){
            viewModel._emailStateFlow.collectLatest { uiState->

                when(uiState)
                {
                    is UIState.Error->  updateUiAfterFaillure()
                    is UIState.Success -> updateUiAfterSuccess(uiState.data)
                    is UIState.Loading  -> loadingDialog.showDialog()
                    else -> loadingDialog.hideDialog()

                }

            }
        }

    }

    private fun resendCode() {

        when(args.source){


            Source.REGISTER.indice -> resendCodeForRegister()
            Source.FORGETPASSWORD.indice -> resendForForgetPassword()


        }


    }

    private fun resendForForgetPassword() {
        binding.resend.setOnClickListener {
            lifecycleScope.launch(IO){
                viewModel.resendOtpCodeForForgetPassword(args.email!!)

            }

        }
    }

    private fun resendCodeForRegister() {
        binding.resend.setOnClickListener {
            lifecycleScope.launch(IO){
                args.email!!
                args.username!!
                viewModel.resendOtpCode(RegisterRequestModel(args.username!! , args.email!! , args.password !!) )

            }

        }
    }

    private fun handleConfirmBt() {
        binding.confirmBt.setOnClickListener {

            val code = binding.code.editableText.toString()
            lifecycleScope.launch(IO)
            {
                val forResult = args.source == 1

                val result =viewModel.confirmEmail(ConfirmRequestModel(args.email!!.trimEnd(), code), forResult)



            }


        }
    }

    private fun updateUiAfterFaillure() {
        Toasty.error(requireActivity(),getString(R.string.otp_false_code),Toast.LENGTH_LONG).show()
        loadingDialog.hideDialog()
    }

    private fun returnToLoginScreen() {
        handleSystemBackButton()

    }



    private fun returnToLoginScreenImp() {

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ConfirmEmailOtpFragmentDirections.actionEmailOtpFragmentToLoginFragment()
        navHostFragment.navController.navigate(action)


    }

    private fun handleSystemBackButton() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    returnToLoginScreenImp()
                }
            }
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

    }



    private fun updateUiAfterSuccess(data: String?) {
        lifecycleScope.launch(Main)
        {



            when (args.source) {
            Source.REGISTER.indice -> {
                navigateTOReminderScreen()
                saveJwtLocally(jwt=data!!, context = requireContext())
                loadingDialog.hideDialog()
            }
            Source.FORGETPASSWORD.indice -> navigateToNewPasswordScreen()
        }
        }
    }

    private fun navigateToNewPasswordScreen() {

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = ConfirmEmailOtpFragmentDirections.actionEmailOtpFragmentToNewPasswordFragment()
        navHostFragment.navController.navigate(action)

    }

    private fun navigateTOReminderScreen() {
        lifecycleScope.launch(Main){


            val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action =
            ConfirmEmailOtpFragmentDirections.actionEmailOtpFragmentToReminderFragment()
        navHostFragment.navController.navigate(action)
    }
    }


    enum class Source(val indice: Int) {
        FORGETPASSWORD(0),
        REGISTER(1)
    }

}

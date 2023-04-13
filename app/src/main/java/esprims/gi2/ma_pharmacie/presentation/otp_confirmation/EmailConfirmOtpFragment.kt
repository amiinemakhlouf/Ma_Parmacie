package esprims.gi2.ma_pharmacie.presentation.otp_confirmation


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.Result.Success
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentEmailOtpBinding
import esprims.gi2.ma_pharmacie.dto.ConfirmDto
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.useCase.saveJwtLocally
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class EmailConfirmOtpFragment : Fragment() {

    private lateinit var binding: FragmentEmailOtpBinding
    private val viewModel: EmailOtpViewModel by viewModels()
    private val args: EmailConfirmOtpFragmentArgs by navArgs()
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        returnToLoginScreen()
        handleConfirmBt()
        resendCode()


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
                withContext(Main){
                    Toast.makeText(requireActivity(),"code envoyé",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun resendCodeForRegister() {
        binding.resend.setOnClickListener {
            lifecycleScope.launch(IO){
                args.email!!
                args.username!!
                viewModel.resendOtpCode(RegisterDto(args.username!! , args.email!! , args.password !!) )
                withContext(Main){
                    Toast.makeText(requireActivity(),"code envoyé",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun handleConfirmBt() {
        binding.confirmBt.setOnClickListener {

            val code = binding.code.editableText.toString()
            lifecycleScope.launch(IO)
            {
                val forResult = args.source == 1

                val result =viewModel.confirmEmail(ConfirmDto(args.email!!.trimEnd(), code), forResult)
                withContext(Main)
                {
                    when (result) {

                        is Success -> {

                            withContext(IO){
                                saveJwtLocally(context=requireContext(),jwt=result.message!!)

                            }
                            updateUiAfterSuccess()
                        }
                        else -> updateUiAfterFaillure()
                    }

                }

            }


        }
    }

    private fun updateUiAfterFaillure() {
        Toast.makeText(requireActivity(), getString(R.string.otp_false_code), Toast.LENGTH_SHORT)
            .show()
    }

    private fun returnToLoginScreen() {
        handleSystemBackButton()

    }

    private fun handleAppBackButton() {
        (requireActivity() as MainActivity).binding.topAppBar.setNavigationOnClickListener {
            returnToLoginScreenImp()
        }
    }

    private fun returnToLoginScreenImp() {

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = EmailConfirmOtpFragmentDirections.actionEmailOtpFragmentToLoginFragment()
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



    private fun updateUiAfterSuccess() {
        lifecycleScope.launch(Main)
        {


        when (args.source) {
            Source.REGISTER.indice -> navigateTOReminderScreen()
            Source.FORGETPASSWORD.indice -> navigateToNewPasswordScreen()
        }
        }
    }

    private fun navigateToNewPasswordScreen() {

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = EmailConfirmOtpFragmentDirections.actionEmailOtpFragmentToNewPasswordFragment()
        navHostFragment.navController.navigate(action)

    }

    private fun navigateTOReminderScreen() {
        lifecycleScope.launch(Main){


            Toasty.success(requireActivity(), "Bienvenue", Toast.LENGTH_SHORT, true).show();
            val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action =
            EmailConfirmOtpFragmentDirections.actionEmailOtpFragmentToReminderFragment()
        navHostFragment.navController.navigate(action)
    }
    }


    enum class Source(val indice: Int) {
        FORGETPASSWORD(0),
        REGISTER(1)
    }

}

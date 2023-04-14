package esprims.gi2.ma_pharmacie.presentation.register

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentRegisterBinding
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadingDialog= LoadingDialog(requireActivity() as MainActivity)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog.hideDialog()
        moveBackToLoginScreen()
        handleRegisterBtLogic()
        updateUiAfterRegisterFlow()
        clearErrorMessageWhenUSerTyping()

    }

    private fun updateUiAfterRegisterFlow() {
        lifecycleScope.launch(Main){


        viewModel._registerStateFlow.collectLatest {uiState->

          when(uiState)
          {
              is UIState.Loading ->{
                  loadingDialog.showDialog()
              }
              is UIState.Success ->{
                  updateUiAfterRegisterSuccess()
                  moveToConfirmRegistration()


              }

              is UIState.Error ->{
                  uiState.message?.let { updateUiAfterRegisterFailed(it) }
                  loadingDialog.hideDialog()

              }

          }

        }
        }

    }

    private fun handleRegisterBtLogic() {
        binding.registerBt.setOnClickListener {
            binding.registerBt.isActivated = false
            if (isInputsValid()) {
                val userDto = getUserDto()
                lifecycleScope.launch(IO) {
                    viewModel.register(userDto)
            }
        }
    }}

    private fun updateUiAfterRegisterFailed(errorMessage:String) {
        binding.registerBt.visibility= INVISIBLE

        val handler = Handler()
        handler.postDelayed(Runnable { // Dismiss the Toast message
            binding.registerBt.visibility=VISIBLE
        }, 2000.toLong()) // 3000 milliseconds = 3 seconds


        Toasty.error(requireActivity(),errorMessage,Toast.LENGTH_SHORT).show()
    }


    fun isInputsValid(): Boolean {
        var isFormValid = true

        if (!handleUserNameInput()) {
            isFormValid = false
        }
        if (!handleEmailInput()) {
            isFormValid = false
        }
        if (!handlePasswordMatching()) {
            isFormValid = false
        }
        if (!handleConfirmPassword()) {
            isFormValid = false
        }




        return isFormValid

    }

    private fun handleConfirmPassword(): Boolean {

        val password = binding.password.editText?.text.toString()
        val confirmPassword = binding.confirmPassword.editText?.text.toString()

        if (!Utils.isPasswordMatches(password, confirmPassword)) {
            binding.confirmPasswordError.visibility = View.VISIBLE
            return false
        }
        binding.confirmPasswordError.visibility = View.INVISIBLE

        return true


    }

    private fun handleUserNameInput(): Boolean {
        if (Utils.isInputEmpty(binding.usernameET)) {

            binding.usernameError.visibility = View.VISIBLE
            return false
        }

        binding.usernameError.visibility = View.INVISIBLE
        return true

    }

    private fun moveToConfirmRegistration() {


        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val email = binding.emailEt.editText!!.text.toString().trimEnd()
        val username = binding.usernameET.editText!!.text.toString()
        val password =binding.password.editText!!.text.toString()
        val action =
            RegisterFragmentDirections.actionRegisterFragmentToEmailOtpFragment(1, email, username,password)
        navHostFragment.navController.navigate(action)


    }

    private fun handleEmailInput(): Boolean {
        if (!Utils.isEmailValid(binding.emailEt.editText?.text.toString().trimEnd())) {

            binding.emailError.visibility = View.VISIBLE
            return false
        }
        binding.emailError.visibility = View.INVISIBLE
        return true
    }

    private fun handlePasswordMatching(): Boolean {
        if (!Utils.isPasswordValid(binding.password.editText?.text.toString())) {

            binding.passwordError.visibility = View.VISIBLE
            return false
        }
        binding.passwordError.visibility = View.INVISIBLE
        return true

    }

    private fun moveBackToLoginScreen() {
        binding.login.setOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            navHostFragment.navController.navigate(action)

        }
    }

    private fun clearErrorMessageWhenUSerTyping() {

        binding.emailEt.editText?.doOnTextChanged { text, start, before, count ->

            binding.emailError.visibility = View.INVISIBLE

        }
        binding.password.editText?.doOnTextChanged { text, start, before, count ->
            binding.passwordError.visibility = View.INVISIBLE
        }
        binding.usernameET.editText?.doOnTextChanged { text, start, before, count ->
            binding.usernameError.visibility = View.VISIBLE

        }
        binding.usernameET.editText?.doOnTextChanged { text, start, before, count ->
            binding.usernameError.visibility = INVISIBLE
        }
        binding.password.editText?.doOnTextChanged { text, start, before, count ->
            binding.confirmPasswordError.visibility = INVISIBLE
        }

    }

    private fun updateUiAfterRegisterSuccess() {
        Toasty.success(requireActivity(),"une dernière étape",Toast.LENGTH_SHORT).show()
        binding.emailError.text = ""
        binding.emailError.visibility = View.INVISIBLE

        binding.emailEt.boxStrokeColor = requireActivity().resources.getColor(R.color.dark_green)
        loadingDialog.hideDialog()

    }

    private fun getUserDto(): RegisterDto {
        val username = binding.usernameET.editText!!.text.toString()
        val email = binding.emailEt.editText!!.text.toString().trimEnd()
        val password = binding.password.editText!!.text.toString()
        val registerDto = RegisterDto(
            username = username,
            email = email, password = password
        )
        return registerDto
    }


}
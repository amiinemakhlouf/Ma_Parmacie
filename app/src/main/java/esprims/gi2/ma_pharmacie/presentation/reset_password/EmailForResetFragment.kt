package esprims.gi2.ma_pharmacie.presentation.reset_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.Result
import esprims.gi2.ma_pharmacie.databinding.FragmentResetPasswordEmailBinding
import esprims.gi2.ma_pharmacie.dto.ForgetPasswordDto
import esprims.gi2.ma_pharmacie.presentation.register.Utils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class EmailForResetFragment : Fragment() {
    private lateinit var binding:FragmentResetPasswordEmailBinding
    private  val viewModel :EmailForResetViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentResetPasswordEmailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearErrorMessageWhenTyping()
        binding.confirmBt.setOnClickListener {

            if(!isEmailinValidForm())
            {
                return@setOnClickListener
            }
            lifecycleScope.launch(IO)
            {
                val email=binding.emailEt.editText?.text.toString().trimEnd()

                val result=viewModel.sendEmailToGetOtpCode(ForgetPasswordDto(email = email))
                withContext(Main)
                {
                    when(result)
                    {
                        is Result.Success -> {
                            updateUiAfterSucces(email)
                        }
                        else -> updateUIAfterFailure((result as Result.Error).message)
                    }
                }
            }
        }
    }

    private fun updateUIAfterFailure(error:String) {
        binding.emailEt.helperText=error
    }

    private fun updateUiAfterSucces(email: String) {
        moveToOtPConfirmation(email)
    }

    private fun moveToOtPConfirmation(email:String) {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = EmailForResetFragmentDirections.actionResetPasswordFragmentToEmailOtpFragment(0,email,null,null)
        navHostFragment.navController.navigate(action)
  }

    fun clearErrorMessageWhenTyping()
    {
        binding.emailEt.editText?.doOnTextChanged { text, start, before, count ->
            binding.emailEt.helperText=""
        }
    }
    fun isEmailinValidForm():Boolean
    {
        val email =binding.emailEt.editText?.text.toString().trimEnd()
        if(!Utils.isEmailValid(email)){
            binding.emailEt.helperText=getString(R.string.email_invalid)
            return false
        }
        return true
    }
}
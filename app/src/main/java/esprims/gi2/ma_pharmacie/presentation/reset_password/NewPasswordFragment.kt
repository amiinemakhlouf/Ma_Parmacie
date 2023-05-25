package esprims.gi2.ma_pharmacie.presentation.reset_password

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentNewPasswordBinding
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.register.Utils
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class NewPasswordFragment : Fragment() {

    private lateinit var binding: FragmentNewPasswordBinding
    private val viewModel:ResetPasswordViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.resetBt.setOnClickListener {

            if (isInputValid()) {
                val email = binding.passwordEt.editText?.text?.toString()

                lifecycleScope.launch(IO) {

                   val result =viewModel.resetPassword(
                       LoginRequestModel(
                           "amiinemakhlouf@gmail.com",
                           email!!
                       )
                   )
                    withContext(Main)
                    {
                    when (result) {
                        is Result.Success -> updateUiAfterSucces()
                        else -> Toast.makeText(requireActivity(),"error occured",Toast.LENGTH_SHORT).show()
                    }
                    }
                }

            }

        }

        clearErrorWhenTyping()
    }

    private fun updateUiAfterSucces() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action =
            NewPasswordFragmentDirections.actionNewPasswordFragmentToLoginFragment()
        navHostFragment.navController.navigate(action)

        Toast.makeText(requireActivity(), "yes is valid", Toast.LENGTH_SHORT).show()

    }

    fun isInputValid():Boolean {

        var isValid = true
        val password = binding.passwordEt.editText?.text.toString()

        val confirmPassword = binding.confirmPasswordEt.editText?.text.toString()
        if (!Utils.isPasswordValid(password)) {

            binding.passwordError.visibility = VISIBLE
            isValid = false

        }
        else{
           binding.passwordError.visibility = INVISIBLE
            Log.d("password",password)

        }
        if(!Utils.isPasswordMatches(password,confirmPassword))
        {
            binding.confirmPasswordError.visibility= VISIBLE
            isValid=false
        }
        else{
            binding.confirmPasswordError.visibility= INVISIBLE
        }
        return isValid
    }

    fun clearErrorWhenTyping()
    {
        binding.passwordEt.editText?.doOnTextChanged { text, start, before, count ->
            binding.passwordError.visibility=INVISIBLE
        }
        binding.confirmPasswordEt.editText?.doOnTextChanged { text, start, before, count ->
            binding.confirmPasswordError.visibility= INVISIBLE
        }
    }


}



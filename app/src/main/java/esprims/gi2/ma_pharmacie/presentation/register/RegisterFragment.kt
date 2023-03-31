package esprims.gi2.ma_pharmacie.presentation.register

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.Result
import esprims.gi2.ma_pharmacie.databinding.FragmentRegisterBinding
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.presentation.main.MainActivityViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerBt.setOnClickListener {
            if (isInputsValid()) {
                val username=binding.usernameET.editText!!.text.toString()
                val email=binding.emailEt.editText!!.text.toString()
                val password=binding.password.editText!!.text.toString()
                val registerDto=RegisterDto(username,email,password)
                lifecycleScope.launch(IO){
                  val result=  viewModel.register(registerDto)

                    withContext(Main){
                        when(result)
                        {
                            is Result.Success ->Toast.makeText(requireActivity(),"succes",Toast.LENGTH_SHORT).show()

                            is Result.Error   -> Toast.makeText(requireActivity(),result.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } else {
                Toast.makeText(requireContext(), "is not valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isInputsValid(): Boolean {
        var isFormValid = true

        if(!handleUserNameInput()){
            isFormValid =false
        }
        if(!handleEmailInput()){
            isFormValid=false
        }
        if(!handlePassword()){
            isFormValid=false
        }
        if(!handleConfirmPassword())
        {
            isFormValid=false
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

           return  true



    }

    private fun handleUserNameInput(): Boolean {
        if (Utils.isInputEmpty(binding.usernameET)) {

            binding.usernameError.visibility = View.VISIBLE
            return false
        }

        binding.usernameError.visibility = View.INVISIBLE
        return true

    }

    private fun handleEmailInput():Boolean
    {
        if (!Utils.isEmailValid(binding.emailEt.editText?.text.toString())) {

            binding.emailError.visibility = View.VISIBLE
            return false
        }
        binding.emailError.visibility = View.INVISIBLE
        return true
    }
     fun handlePassword():Boolean
     {
         if (!Utils.isPasswordValid(binding.password.editText?.text.toString())) {

             binding.passwordError.visibility = View.VISIBLE
             return  false
         }
             binding.passwordError.visibility = View.INVISIBLE
         return  true

     }

}
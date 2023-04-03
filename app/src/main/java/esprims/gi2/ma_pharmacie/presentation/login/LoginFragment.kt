package esprims.gi2.ma_pharmacie.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentLoginBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.Result
import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private  val TAG ="Login fragment"
    private  lateinit var  binding:FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideAppBar(requireActivity() as AppCompatActivity)
        hideKeyboardWhenUserTouchOut()
        handleLogin()
        handleGoogleSignIn()
        moveToRegisterScreen()


    }

    private fun handleLogin() {
        binding.loginBt.setOnClickListener {


            if(isInputsValid()){
                val email=binding.emailEt.editText?.text.toString()
                val password=binding.password.editText?.text.toString()
                lifecycleScope.launch(IO)
                {
                    val result=viewModel.loginWithEmailAndPassword(LoginDto(email=email,password=password))
                    withContext(Main)
                    {
                        when(result)
                        {
                            is Result.Success ->{
                                moveToMainScreen()
                                Toast.makeText(requireActivity(),getString(R.string.welcome),Toast.LENGTH_SHORT).show()
                            }

                            is Result.Error ->{
                                when(result.message){
                                    "user not exist"  ->{
                                        binding.emailEt.helperText=getString(R.string.Invalid_email_address)
                                    }
                                    "wrong password"  ->binding.password.helperText=getString(R.string.wrong_password)
                                }

                            }
                        }
                    }
                }


            }


        }

    }

    private fun moveToMainScreen() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        val action = LoginFragmentDirections.actionLoginFragmentToReminderFragment()
        navHostFragment.navController.navigate(action)
    }

    private  fun isInputsValid():Boolean{
        var inputsAreValid=true
        if(!emailHandler() )
        {
           inputsAreValid =false
        }
        if (!passwordHandler())
        {
            inputsAreValid=false
        }
        if(inputsAreValid)
        {
            return true
        }

            return false

    }

    private fun emailHandler():Boolean
    {

        if(binding.emailEt.editText?.text.isNullOrBlank()){

            binding.emailEt.helperText="merci de remplir ce champs"

            return false

        }
        binding.emailEt.helperText=""
        return true

    }

    private fun passwordHandler():Boolean
    {
        if(binding.password.editText?.text.isNullOrBlank())
        {
            binding.password.helperText="merci de remplir ce champs"
            return false
        }
        binding.password.helperText=""
        return true

    }

    private fun handleGoogleSignIn(){
        binding.googleSignIn.setOnClickListener{


        viewModel.loginWithGoogle()
        val options=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.default_web_client_id)).build()
        val signInClient= GoogleSignIn.getClient(requireContext(),options)
        signInClient.signInIntent.also {
            startActivityForResult(it,0)
        }
        }
    }

    private fun hideKeyboardWhenUserTouchOut(){

        binding.root.setOnClickListener { it ->
            requireActivity().hideKeyboard(it)
        }

    }
    private  fun moveToRegisterScreen()

    {
        binding.createNewAccount.setOnClickListener{
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            navHostFragment.navController.navigate(action)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0)
        {
            val account=GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("jojo,",account.getResult().idToken!!)
            moveToMainScreen()


        }
    }

}


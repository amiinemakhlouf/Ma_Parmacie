package esprims.gi2.ma_pharmacie.presentation.login

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.databinding.FragmentLoginBinding
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.onBoarding.dataStore
import esprims.gi2.ma_pharmacie.presentation.register.Utils
import esprims.gi2.ma_pharmacie.presentation.shared.Constants
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar
import esprims.gi2.ma_pharmacie.useCase.saveJwtLocally
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var loadingDialog: LoadingDialog
    private  val TAG ="Login fragment"
    private  lateinit var  binding:FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hidBottomNavigation()
        loadingDialog=LoadingDialog(requireActivity() as MainActivity)
        binding= FragmentLoginBinding.inflate(layoutInflater)
        val text="dqsdqs"
        if((requireActivity() as MainActivity).isFRomReminder==true)
        {
            (requireActivity() as MainActivity).isFRomReminder=false
            return  binding.root
        }


        lifecycleScope.launch(IO){



            if(userIsLoggedIn()){

                withContext(Main){
                    Log.d("bogi","i'm logged in ")

                    //moveToReminderScreen()




                }
            }
            else {
                withContext(Main){

                    (requireActivity() as MainActivity).binding.root.visibility= View.VISIBLE
                    loadingDialog.hideDialog()


                }
            }
        }





        return binding.root
    }

    private fun hidBottomNavigation() {
        ( requireActivity() as MainActivity).binding.bottomNavView.visibility= GONE
        ( requireActivity() as MainActivity).binding.fab.visibility= GONE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideAppBar(requireActivity() as AppCompatActivity)
        checkCredentials()
        updateUiLoginFlow()
        hideKeyboardWhenUserTouchOut()
        handleGoogleSignIn()
        moveToRegisterScreen()
        moveToForgetPassword()
        clearErrorMessageWhenTyping()



    }

    @SuppressLint("ResourceType")
    private fun updateUiLoginFlow() {
        lifecycleScope.launch(Main) {

            viewModel._loginState.collect { uiState ->
                when (uiState) {

                    is UIState.Success -> {
                        loadingDialog.hideDialog()
                        uiState.data?.let {
                            saveJwtLocally(it,requireActivity())
                        }
                        moveToReminderScreen()
                    }
                    is UIState.Loading ->{
                        loadingDialog.showDialog()

                    }

                    is UIState.Error ->{
                        loadingDialog.hideDialog()
                        uiState.errorMessage.let { Toasty.error(requireActivity(), it,Toast.LENGTH_LONG).show() }
                        //showSnackBar(getString(R.string.no_internet))
                    }
                }
            }
        }
    }

    private fun showSnackBar(message:String) {
        Snackbar.make(binding.root,message,Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(android.R.color.darker_gray,null))
            .setTextColor(resources.getColor(R.color.white))
            .show()
    }


    private fun moveToForgetPassword() {
        binding.forgetPassword.setOnClickListener{
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
            navHostFragment.navController.navigate(action)
        }
    }

    private fun checkCredentials() {
        binding.loginBt.setOnClickListener {
             if(isInputsValid()){
                val email=binding.emailEt.editText?.text.toString().trimEnd()
                val password=binding.passwordEt.editText?.text.toString()
                loadingDialog.showDialog()
                viewModel.loginWithEmailAndPassword(LoginRequestModel(email=email,password=password))

                }

        }
        }



   private fun moveToReminderScreen() {


            loadingDialog.hideDialog()

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
        else if(!Utils.isEmailValid(binding.emailEt.editText?.text.toString().trimEnd())){
            binding.emailEt.helperText="email invalide"
            return false
        }
        else{
            binding.emailEt.helperText=""
        }

        return true

    }

    private fun passwordHandler():Boolean
    {
        val emptyFieldErrorMessage="merci de remplir ce champs"
        val invalidPasswordErrorMessage="mot de passe doit contenir plus de 8  carachteres"
        val clearMessage= ""
        if(binding.passwordEt.editText?.text.isNullOrBlank())
        {
            binding.passwordEt.helperText=emptyFieldErrorMessage
            return false
        }
        else if(!Utils.isPasswordValid(password = binding.passwordEt.editText?.text.toString()))
        {
            binding.passwordEt.helperText=invalidPasswordErrorMessage
            return false
        }
        else{
            binding.passwordEt.helperText=clearMessage

        }
        return true

    }

    private fun handleGoogleSignIn(){
        binding.googleSignIn.setOnClickListener{


        val options=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().requestIdToken(getString(R.string.default_web_client_id)).build()

        val signInClient= GoogleSignIn.getClient(requireContext(),options)
        signInClient.signInIntent.also {
           // startActivityForResult(it,0)
            startForResult.launch(it)
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
    private fun clearErrorMessageWhenTyping()
    {
        val clearMessage= ""
        binding.emailEt.editText?.doOnTextChanged { text, start, before, count ->
            binding.emailEt.helperText=clearMessage
        }
        binding.passwordEt.editText?.doOnTextChanged { text, start, before, count ->
            binding.passwordEt.helperText=clearMessage
        }
    }
    suspend private fun userIsLoggedIn(): Boolean {
         val key="jwt"
        val jwtKey = stringPreferencesKey(key)

        val preferences= requireContext().dataStore.data.first()
        if(!preferences[jwtKey].isNullOrEmpty()){

            return true
        }

        return false




    }
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            try {


                val account= GoogleSignIn.getSignedInAccountFromIntent(intent)
                val userData =account.result
                val username=userData.displayName
                val email=userData.email
                Constants.EMAIL=email!!
                lifecycleScope.launch(IO){
                    viewModel.loginWithGoogleAccount(User(username=username!!,email=email!!))

                }
    }
            catch(e:java.lang.Exception){

                loadingDialog.hideDialog()

            }
        }
    }
}


package esprims.gi2.ma_pharmacie.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.onBoarding.dataStore
import esprims.gi2.ma_pharmacie.presentation.register.Utils
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.presentation.shared.hideAppBar
import esprims.gi2.ma_pharmacie.useCase.saveJwtLocally
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var progressDialog: AlertDialog
    private  val TAG ="Login fragment"
    private  lateinit var  binding:FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val alertDialogBuilder=AlertDialog.Builder(requireContext())
        progressDialog= alertDialogBuilder.setView(R.layout.custom_progress_bar).show()



        (  requireActivity() as MainActivity).binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding= FragmentLoginBinding.inflate(layoutInflater)
        if((requireActivity() as MainActivity).isFRomReminder==true)
        {
            (requireActivity() as MainActivity).isFRomReminder=false
            progressDialog.hide()
            return  binding.root
        }
        (requireActivity() as MainActivity).binding.drawer.visibility= View.INVISIBLE






        lifecycleScope.launch(IO){



            if(userIsLoggedIn()){

                withContext(Main){
                    Log.d("bogi","i'm logged in ")

                    moveToReminderScreen()




                }
            }
            else {
                withContext(Main){

                    (requireActivity() as MainActivity).binding.root.visibility= View.VISIBLE
                    progressDialog.hide()


                }
            }
        }





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideAppBar(requireActivity() as AppCompatActivity)
        checkCredentials()
        updateUiLoginFlow()
        disableDrawer()
        hideKeyboardWhenUserTouchOut()
        handleGoogleSignIn()
        moveToRegisterScreen()
        moveToForgetPassword()
        clearErrorMessageWhenTyping()



    }

    @SuppressLint("ResourceType")
    private fun updateUiLoginFlow() {
        lifecycleScope.launch(Main) {

            viewModel._loginState.collectLatest { uiState ->
                when (uiState) {

                    is UIState.Success -> {
                        progressDialog.hide()
                        Toasty.success(requireActivity(), "Bienvenue", Toast.LENGTH_SHORT, true).show();
                        uiState.data?.let {
                            saveJwtLocally(it,requireActivity())
                        }
                        moveToReminderScreen()
                    }
                    is UIState.Loading ->{
                        progressDialog.show()

                    }

                    is UIState.Error ->{
                        progressDialog.hide()
                        uiState.message?.let { Toasty.error(requireActivity(), it,Toast.LENGTH_LONG).show() }
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

    private fun disableDrawer() {
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
                val password=binding.password.editText?.text.toString()
                progressDialog.show()

                viewModel.loginWithEmailAndPassword(LoginDto(email=email,password=password))

                }

        }
        }



   private fun moveToReminderScreen() {


            progressDialog.hide()

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
        if(binding.password.editText?.text.isNullOrBlank())
        {
            binding.password.helperText="merci de remplir ce champs"
            return false
        }
        else if(!Utils.isPasswordValid(password = binding.password.editText?.text.toString()))
        {
            binding.password.helperText="mot de passe doit contenir plus de 8  carachteres"
            return false
        }
        else{
            binding.password.helperText=""

        }
        return true

    }

    private fun handleGoogleSignIn(){
        binding.googleSignIn.setOnClickListener{


        val options=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().requestIdToken(getString(R.string.default_web_client_id)).build()

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
    private fun clearErrorMessageWhenTyping()
    {
        binding.emailEt.editText?.doOnTextChanged { text, start, before, count ->
            binding.emailEt.helperText=""
        }
        binding.password.editText?.doOnTextChanged { text, start, before, count ->
            binding.password.helperText=""
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0)
        {
            try {


            val account= GoogleSignIn.getSignedInAccountFromIntent(data)
            val userData =account.result
            val username=userData.displayName
            val email=userData.email
            lifecycleScope.launch(IO){
                viewModel.loginWithGoogleAccount(User(username=username!!,email=email!!))

            }



            }
            catch(e:java.lang.Exception){

                progressDialog.hide()

            }
        }

    }

    suspend private fun userIsLoggedIn(): Boolean {

        val jwtKey = stringPreferencesKey("jwt")

        val preferences= requireContext().dataStore.data.first()
        if(!preferences[jwtKey].isNullOrEmpty()){

            return true
        }

        return false




    }}


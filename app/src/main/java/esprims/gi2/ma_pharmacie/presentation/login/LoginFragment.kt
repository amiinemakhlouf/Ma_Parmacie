package esprims.gi2.ma_pharmacie.presentation.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_OPEN
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentLoginBinding
import esprims.gi2.ma_pharmacie.presentation.hideKeyboard
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.main.MainActivityViewModel
import esprims.gi2.ma_pharmacie.presentation.onBoarding.OnBoardingFragmentDirections
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class LoginFragment : Fragment() {
    private  val TAG ="Login fragment"
    private  lateinit var  binding:FragmentLoginBinding
    private val viewModel: MainActivityViewModel by viewModels()
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
        viewModel.navigateToLogin=true




        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.root.setOnClickListener { it ->
           requireActivity().hideKeyboard(it)
        }
        binding.loginBt.setOnClickListener {

           /* val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = LoginFragmentDirections.actionLoginFragmentToReminderFragment()
            navHostFragment.navController.navigate(action)*/
            checkInputs()


        }
        binding.createNewAccount.setOnClickListener{
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            navHostFragment.navController.navigate(action)
               }

    }

    private  fun checkInputs(){
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
            Toast.makeText(requireActivity(),"inputs are valid",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(requireActivity(),"inputs are not valid",Toast.LENGTH_SHORT).show()
        }
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

}


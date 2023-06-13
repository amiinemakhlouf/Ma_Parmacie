package esprims.gi2.ma_pharmacie.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentSplashBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.onBoarding.dataStore
import esprims.gi2.ma_pharmacie.presentation.shared.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashFragment : Fragment() {
    private  val fragmentSplashBinding :FragmentSplashBinding by lazy {
        FragmentSplashBinding.inflate(layoutInflater)
    }
    private  val loadingDialog:LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return fragmentSplashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).binding.fab.visibility=GONE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility= GONE
        loadingDialog.showDialog()
        handleStartScreen()

    }

    private  fun handleStartScreen()
    {
        lifecycleScope.launch(Dispatchers.IO) {
            val  isUserLogin=userIsLoggedIn()
            val navHostFragment=requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            withContext(Dispatchers.Main)
            {
                if(isUserLogin){
                    loadingDialog.hideDialog()
                    val action=SplashFragmentDirections.actionSplashFragmentToReminderFragment()
                    navHostFragment.findNavController().navigate(action)

                }
                else{
                    loadingDialog.hideDialog()
                   val action=SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    navHostFragment.navController.navigate(action)

                }
            }


        }
    }
    suspend private fun userIsLoggedIn(): Boolean {


        val jwtKey = stringPreferencesKey("jwt")

        val preferences= requireActivity().dataStore.data.first()
        if(!preferences[jwtKey].isNullOrEmpty()){

            return true
        }

        return false

    }


}
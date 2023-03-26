package esprims.gi2.ma_pharmacie.presentation.onBoarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentOnBoardingBinding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class OnBoardingFragment : Fragment() {

  private  lateinit var  binding:FragmentOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding= FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking{
            shouldNavigateToLogin()?.let {
               binding.progressBar.visibility=INVISIBLE
                val navHostFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.loginFragment)



                return@let


            }
            if (shouldNavigateToLogin()==null){
                saveFirstStartUp()
            }

        }
        val fragmentList = listOf<Fragment>(
            FirstOnBoardingFragment(),
            SecondOnBoardingFragment(),
            ThirdOnBoardingFragment()
        )

        val adapter= OnBoardingAdapter(requireActivity().supportFragmentManager
            ,lifecycle ,fragmentList)
        val viewPager= requireActivity().findViewById<ViewPager2>(R.id.onboarding_view_pager)
        viewPager.adapter=adapter





    }
    private suspend fun shouldNavigateToLogin():Boolean?
    {
        val booleanSaveValue= booleanPreferencesKey("save")
        val preferences=requireActivity().dataStore.data.first()
        return  preferences[booleanSaveValue]
    }

    private fun saveFirstStartUp() {
        val booleanSaveValue= booleanPreferencesKey("save")
        lifecycleScope.launch(IO){
            requireActivity().dataStore.edit { settings ->

                settings[booleanSaveValue] = true
            }
        }

    }





}
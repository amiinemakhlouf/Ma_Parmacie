package esprims.gi2.ma_pharmacie.presentation.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentThirdOnBoardingBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity


class ThirdOnBoardingFragment : Fragment() {
    private lateinit var binding:FragmentThirdOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          binding= FragmentThirdOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextText.setOnClickListener {
            navigateToLoginScreen()
        }

        val viewPager2= requireActivity().findViewById<ViewPager2>(R.id.onboarding_view_pager)




    }

    private fun navigateToLoginScreen() {
        val action =OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
        navHostFragment.navController.navigate(action)

    }


}
package esprims.gi2.ma_pharmacie.presentation.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentFirstOnBoardingBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import esprims.gi2.ma_pharmacie.presentation.register.RegisterFragmentDirections


class FirstOnBoardingFragment : Fragment() {

    private lateinit var binding:FragmentFirstOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFirstOnBoardingBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager= requireActivity().findViewById<ViewPager2>(R.id.onboarding_view_pager)
        binding.nextText.setOnClickListener {
            viewPager.currentItem+=1
        }
        binding.skipText.setOnClickListener {
            val action =OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.my_fragment) as NavHostFragment
            navHostFragment.navController.navigate(action)

        }

    }
}
package esprims.gi2.ma_pharmacie.presentation.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentSecondOnBoardingBinding


class SecondOnBoardingFragment : Fragment() {
   private  lateinit var binding:FragmentSecondOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSecondOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager= requireActivity().findViewById<ViewPager2>(R.id.onboarding_view_pager)
        binding.nextText.setOnClickListener {
            viewPager.currentItem+=1
        }


    }



}
package esprims.gi2.ma_pharmacie.presentation.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentThirdBinding


class ThirdOnBoardingFragment : Fragment() {
    private lateinit var binding:FragmentThirdBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          binding= FragmentThirdBinding.inflate(layoutInflater)
        return binding.root
    }


}
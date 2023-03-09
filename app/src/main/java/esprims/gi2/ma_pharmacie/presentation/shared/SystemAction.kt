package esprims.gi2.ma_pharmacie.presentation.shared

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun onSystemBackClicked(fragment: Fragment)
{
    val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                fragment.findNavController().popBackStack()
            }
        }
     fragment.requireActivity().getOnBackPressedDispatcher().addCallback(fragment.requireActivity(), callback);


}
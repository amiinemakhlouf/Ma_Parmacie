package esprims.gi2.ma_pharmacie.presentation.reminder_detail

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderBinding
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderDetailsBinding
import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked

class ReminderDetailsFragment : Fragment() {

  private  lateinit var binding: FragmentReminderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentReminderDetailsBinding.inflate(layoutInflater)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSystemBackButtonClicked(this)
        disableTopBar()
    }
    private fun disableTopBar()
    {
        ( requireActivity() as AppCompatActivity).supportActionBar!!.hide()
    }


}
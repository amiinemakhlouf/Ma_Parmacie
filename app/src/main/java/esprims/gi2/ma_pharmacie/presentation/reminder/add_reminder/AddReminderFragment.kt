package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentAddReminderBinding


class AddReminderFragment : Fragment() {
    private  lateinit var binding:FragmentAddReminderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddReminderBinding.inflate(layoutInflater)
        return binding.root
    }


}
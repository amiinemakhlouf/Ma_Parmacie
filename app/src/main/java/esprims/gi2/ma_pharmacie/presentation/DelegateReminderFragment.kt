package esprims.gi2.ma_pharmacie.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentDelegateReminderBinding
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity


class DelegateReminderFragment : Fragment() {
    private val fragmentDelegateReminderBinding  :FragmentDelegateReminderBinding by lazy {
        FragmentDelegateReminderBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return fragmentDelegateReminderBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).binding.fab.visibility= GONE
        (requireActivity() as MainActivity).binding.bottomNavView.visibility= GONE
        val textToShare = "My own text\nhttps://stackoverflow.com/"
        fragmentDelegateReminderBinding.backButton.setOnClickListener {
            (requireActivity() as MainActivity).binding.fab.visibility= VISIBLE
            (requireActivity() as MainActivity).binding.bottomNavView.visibility= VISIBLE

        }

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.facebook.orca")
        fragmentDelegateReminderBinding.fb.setOnClickListener {
            requireActivity().startActivity(sendIntent)
        }

    }


}
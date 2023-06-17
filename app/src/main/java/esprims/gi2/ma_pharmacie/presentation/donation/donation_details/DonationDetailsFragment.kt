package esprims.gi2.ma_pharmacie.presentation.donation.donation_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentDonationDetailsBinding


class DonationDetailsFragment : Fragment() {
    private  val binding:FragmentDonationDetailsBinding by lazy {
        FragmentDonationDetailsBinding.inflate(layoutInflater)
    }
    private  val donationDetailsFragmentArgs:DonationDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dropdown.setText(donationDetailsFragmentArgs.donation.city)
        binding.disponibilityETT.setText(donationDetailsFragmentArgs.donation.availability)
        binding.quantityETT.setText(donationDetailsFragmentArgs.donation.quantity)
        binding.continuerFab.setOnClickListener {
            dialPhoneNumber(donationDetailsFragmentArgs.donation.phoneNumber!!)


        }


    }
    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

}
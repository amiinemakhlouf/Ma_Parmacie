package esprims.gi2.ma_pharmacie.presentation.pharmacie

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.BottomSheetPharmacyBinding

class PharmacyBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetPharmacyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= BottomSheetPharmacyBinding.inflate(layoutInflater)
        binding.ratingBar.max=5
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      handleCallPharmacy()
      getPharmacyInformation()


    }

    private fun getPharmacyInformation() {
        val name=arguments?.getString("name")
        val address=arguments?.getString("address")
        val isOpen=arguments?.getBoolean("isOpen")
        val rates =arguments?.getFloat("rates")
        val phoneNumber=arguments?.getString("phoneNumber")
        binding.pharmacyName.text=name
        binding.pharmacyState.text=isOpen.toString()
        isOpen?.let {isOpen->
            if(isOpen)
            {
                binding.pharmacyState.text="ouvert"
                binding.pharmacyState.setTextColor(Color.parseColor("#295918"))
            }
            else{
                binding.pharmacyState.text="fermé"
                binding.pharmacyState.setTextColor(Color.RED)

            }
        }

        binding.pharmacyPhone.text=phoneNumber
        rates?.let {rates->
            binding.ratingBar.rating=rates
        }
        binding.ratingBar.isClickable=false
        binding.ratingBar.isFocusable=false



    }

    private fun handleCallPharmacy()
    {
        callOnCLickPhoneNumber()
        callOnClickPhoneIcon()
    }

    private fun callOnClickPhoneIcon() {
        binding.pharmacyPhone.setOnClickListener {
            val phoneNumber = (it as TextView).text
            val uri = Uri.parse("tel:$phoneNumber")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)

        }
    }

    private fun callOnCLickPhoneNumber() {
        binding.pharmacyPhone.setOnClickListener {
            val phoneNumber = (it as TextView).text
            val uri = Uri.parse("tel:$phoneNumber")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


}
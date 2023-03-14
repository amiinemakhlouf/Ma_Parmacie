package esprims.gi2.ma_pharmacie.presentation.medication.show_medication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentShowMedicationBinding

import esprims.gi2.ma_pharmacie.presentation.shared.onSystemBackButtonClicked

class ShowMedicationFragment : Fragment() {
    private val TAG = "MedicineFragment"
    private lateinit var binding: FragmentShowMedicationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowMedicationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.circle_explosion_anim).apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
            }

        onSystemBackButtonClicked(this)

        binding.fab.setOnClickListener {
            val action =
                ShowMedicationFragmentDirections.actionMedicineFragmentToAddMedicineDialog()

            findNavController().navigate(action)



        }


    }


}
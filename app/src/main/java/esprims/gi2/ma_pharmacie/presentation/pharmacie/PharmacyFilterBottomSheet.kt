package esprims.gi2.ma_pharmacie.presentation.pharmacie

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.FragmentPharmacyFilterBottomSheetBinding


class PharmacyFilterBottomSheet(
    private val  dataPassListener: DataPassListener
)
    : BottomSheetDialogFragment() {
    private  val binding by lazy {
        FragmentPharmacyFilterBottomSheetBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getBoolean("open")

        data?.let {
            binding.openSwitch.isChecked=data
        }
         showDistanceSeekBar()
        binding.filter.setOnClickListener {
            val onlyOpen=binding.openSwitch.isChecked
            val distance=binding.distanceSlider.value
            Toast.makeText(requireContext(),""+distance.toDouble(),Toast.LENGTH_SHORT).show()
            val minimumEvaluation=binding.ratingBar.rating
            dismiss()
            dataPassListener.onDataPassed(onlyOpen,distance,minimumEvaluation)


        }


    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


    private fun showDistanceSeekBar() {
        val labelFormatter = LabelFormatter { value -> // Customize the label text based on the slider value
            // For example, you can display the value as a percentage
            return@LabelFormatter "" +value+"km"
        }

        binding.distanceSlider.setLabelFormatter(labelFormatter)


        binding.distanceSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser -> // Update the label text
            binding.labelTextView.visibility=VISIBLE
            binding.labelTextView.text=labelFormatter.getFormattedValue(value)
        })


    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

}
interface DataPassListener {
    fun onDataPassed(isOpen: Boolean,distance:Float,minimumEvaluation:Float)
}
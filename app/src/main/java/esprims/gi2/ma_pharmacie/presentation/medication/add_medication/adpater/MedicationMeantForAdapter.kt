package esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import esprims.gi2.ma_pharmacie.databinding.MedicationMeantForBinding
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationMeantForItem

/*class MedicationMeantForAdapter(context: Context, val medicationsMeantFor:List<MedicationMeantForItem>
) : ArrayAdapter<MedicationMeantForAdapter>(context,0)
{
    private lateinit var binding: MedicationMeantForBinding




    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val medicationMeantFor=  getItem(position)

        binding= MedicationMeantForBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        binding.sexOrAgeText.text=medicationMeantFor.meantForText.toString()
        val context =parent.context as AppCompatResources
        binding.sexOrAgeIcon.setImageDrawable()
        return (binding.root)
    }

    override fun getCount(): Int = medicationsMeantFor.size

    override fun getItem(position: Int) = medicationsMeantFor[position]




    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return super.getFilter()
    }
}*/
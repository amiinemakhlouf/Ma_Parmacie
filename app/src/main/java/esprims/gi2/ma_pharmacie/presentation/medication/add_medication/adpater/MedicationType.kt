package esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import esprims.gi2.ma_pharmacie.databinding.MedicationTypeItemBinding
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationTypeItem

class MedicationType(context: Context, val medicineTypes:List<MedicationTypeItem>
) : ArrayAdapter<MedicationTypeItem>(context,0)
{
    private lateinit var binding: MedicationTypeItemBinding




    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val medicationType=  getItem(position)

        binding= MedicationTypeItemBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        binding.myTV.text=medicationType.name + medicationType.explanation
        return (binding.root)
    }

    override fun getCount(): Int = medicineTypes.size

    override fun getItem(position: Int) = medicineTypes[position]




    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return super.getFilter()
    }
}
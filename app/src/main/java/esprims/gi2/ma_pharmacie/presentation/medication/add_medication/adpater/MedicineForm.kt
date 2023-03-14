package esprims.gi2.ma_pharmacie.presentation.medication.add_medication.adpater

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import esprims.gi2.ma_pharmacie.databinding.MedicationFormItemBinding
import esprims.gi2.ma_pharmacie.presentation.medication.add_medication.model.MedicationFormItem


class MedicineForm(context:Context, val medicineForms:List<MedicationFormItem>
) : ArrayAdapter<MedicationFormItem>(context,0)
{
    private lateinit var binding:MedicationFormItemBinding




    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val medicationForm=  getItem(position)

        binding= MedicationFormItemBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false)
        binding.logo.setImageDrawable(
            context.applicationContext.getDrawable(medicationForm!!.image))
        binding.title.text=medicationForm.name

        return (binding.root)
    }

    override fun getCount(): Int = medicineForms.size

    override fun getItem(position: Int) = medicineForms[position]




    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    

    override fun getFilter(): Filter {
        return super.getFilter()
    }
}
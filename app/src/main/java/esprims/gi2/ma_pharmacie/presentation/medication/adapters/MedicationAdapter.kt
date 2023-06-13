package esprims.gi2.ma_pharmacie.presentation.medication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.databinding.MedicationItemBinding

class MedicationAdapter (
    val dataset:List<Medication>
        ): RecyclerView.Adapter<MedicationAdapter.ViewHolder>() {
    class ViewHolder (
        val binding:MedicationItemBinding
            ):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (
                ViewHolder(
                    MedicationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
                )
    }

    override fun getItemCount(): Int {
       return  dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.name.setText(dataset[position].name)
        holder.binding.type.setText(dataset[position].type)
        holder.binding.unit.setText(dataset[position].unit)
        holder.binding.quantityValue.setText(dataset[position].quantity?.toInt().toString())

        if(position==dataset.lastIndex){

            holder.layoutPosition
        }


    }
}
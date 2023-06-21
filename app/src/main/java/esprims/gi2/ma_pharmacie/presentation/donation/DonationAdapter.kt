package esprims.gi2.ma_pharmacie.presentation.donation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Medication
import esprims.gi2.ma_pharmacie.databinding.MedicationItemBinding

class MedicationAdapter (
    val dataset:List<Medication>,
    val medicationAdapterListener: MedicationAdapterListener?
): RecyclerView.Adapter<MedicationAdapter.ViewHolder>() {
    class ViewHolder (
        val binding: MedicationItemBinding
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

        val context=holder.itemView.context
        holder.binding.name.setText(dataset[position].name)
        //holder.binding.type.setText(dataset[position].type)
       // holder.binding.unit.setText(dataset[position].unit)
        when(dataset[position].type){
            "capsules" -> holder.binding.illustration.setImageDrawable( context.getDrawable(
                R.drawable.capsule
            ))

            "capsule" -> holder.binding.illustration.setImageDrawable( context.getDrawable(
                R.drawable.capsule
            ))

            "bouteilles"->holder.binding.illustration.setImageDrawable( context.getDrawable(
                R.drawable.liquid
            ))
            "injection"->holder.binding.illustration.setImageDrawable( context.getDrawable(
                R.drawable.injection
            ))
            "injections"->holder.binding.illustration.setImageDrawable( context.getDrawable(
                R.drawable.injection
            ))
            "Comprimé"->holder.binding.illustration.setImageDrawable( context.getDrawable(
                R.drawable.tablet
            ))
            "Comprimés"->holder.binding.illustration.setImageDrawable( context.getDrawable(
                R.drawable.tablet
            ))
        }
        holder.binding.quantityValue.setText(dataset[position].quantity?.toInt().toString())

        holder.binding.root.setOnClickListener {
            medicationAdapterListener?.onItemClick(dataset[position])
        }


    }


    interface  MedicationAdapterListener{

        fun onItemClick(medication:Medication)

    }
}
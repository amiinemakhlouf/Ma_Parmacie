package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.util.Log
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.ReminderChildItemBinding
import esprims.gi2.ma_pharmacie.databinding.ReminderItemBinding
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder

class ReminderChildAdapter(
    private  val dataset:List<Reminder>,
    private val reminderCallback: ReminderCallback

) :RecyclerView.Adapter<ReminderChildAdapter.ViewHolder>(){
    class ViewHolder
        (
        val binding:ReminderChildItemBinding
                ):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ReminderChildItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context=holder.binding.root.context
        val currentItem=dataset[position]
        holder.binding.patientName.text=currentItem.personName
        holder.binding.medicationName.text=currentItem.medicationName
        holder.binding.medicationDose.text=currentItem.dose
        if(currentItem.isDelegated)
        {
            holder.binding.delegate.visibility=INVISIBLE
        }
        Log.d("MedicationAdapter",currentItem.type!!)


        if((currentItem.type==context.getString(R.string.capsule)) ||
            (currentItem.type=="capsules")

                )
        {
            holder.binding.medicationImage.setImageDrawable(context.getDrawable(R.drawable.capsule))
        }
        if((currentItem.type==context.getString(R.string.bottles)) ||
            ( currentItem.type== "bouteilles")
                )
        {
            holder.binding.medicationImage.setImageDrawable(context.getDrawable(R.drawable.liquid))
        }


        if((currentItem.type==context.getString(R.string.pill) )||
            ( currentItem.type=="Comprimés")

        )
        {
            holder.binding.medicationImage.setImageDrawable(context.getDrawable(R.drawable.pill))
        }
        if((currentItem.type==context.getString(R.string.injection) ) ||
            (currentItem.type== "Injections")
        )
        {
            holder.binding.medicationImage.setImageDrawable(context.getDrawable(R.drawable.injection))
        }

        holder.binding.delegate.setOnClickListener {

            reminderCallback.navigateToDelegateScreen(currentItem)
        }
        holder.binding.root.setOnClickListener {
            reminderCallback.navigateToDetailsScreen(currentItem)
        }
    }
}
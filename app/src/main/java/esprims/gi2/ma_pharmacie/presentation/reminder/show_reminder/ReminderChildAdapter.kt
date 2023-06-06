package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        val currentItem=dataset[position]
        holder.binding.patientName.text=currentItem.personName
        holder.binding.medicationName.text=currentItem.medicationName
        holder.binding.medicationDose.text=currentItem.dose

        holder.binding.more.setOnClickListener {
            reminderCallback.navigateToDetailsScreen()
        }
    }
}
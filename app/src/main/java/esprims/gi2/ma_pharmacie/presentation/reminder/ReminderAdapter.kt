package esprims.gi2.ma_pharmacie.presentation.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.DaysItemsBinding
import esprims.gi2.ma_pharmacie.databinding.ReminderItemBinding
import esprims.gi2.ma_pharmacie.presentation.reminder.model.Date
import esprims.gi2.ma_pharmacie.presentation.reminder.model.Reminder

class ReminderAdapter(private  val dataset:List<Reminder>
) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    class ViewHolder(
        val binding: ReminderItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
    override fun getItemCount(): Int {
        return  dataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return  ViewHolder(
            ReminderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem=dataset[position]
        holder.binding.medicationDose.text="15"

    }
}

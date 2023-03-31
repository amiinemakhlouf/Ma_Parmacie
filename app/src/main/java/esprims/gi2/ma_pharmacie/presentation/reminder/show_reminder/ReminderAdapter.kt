package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.ReminderItemBinding
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder

class ReminderAdapter(
    private  val dataset:List<Reminder>,
    private val reminderCallback: ReminderCallback
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

        val context=holder.itemView.context
        holder.binding.medicationNameTv.transitionName=context.resources.getString(R.string.medicationNameTransition)
        holder.binding.reminderDetails.setOnClickListener {


            reminderCallback.navigateToDetailsScreen()


        }

    }
}

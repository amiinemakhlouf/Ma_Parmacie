package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.databinding.ReminderItemBinding
import esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model.Reminder

class ReminderParentAdapter(
    private  val dataset:List<List<Reminder>>,
    private val reminderCallback: ReminderCallback
) : RecyclerView.Adapter<ReminderParentAdapter.ViewHolder>() {
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
        holder.binding.time.text=currentItem[0].reminderTime

        val adapter=ReminderChildAdapter(dataset[position],reminderCallback)
        holder.binding.reminderRecyclerViewParent.apply {
            layoutManager= LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
            this.adapter=adapter

        }
       /* holder.binding.medicationNameTv.setText(currentItem.medicationName)
        holder.binding.reminderTime.setText(currentItem.reminderTime)
        holder.binding.personName.setText(currentItem.personName)
//        holder.binding.medicationDose.setText(currentItem.dose)
        holder.binding.reminderDetails.setOnClickListener {


            reminderCallback.navigateToDetailsScreen()


        }*/

    }
}

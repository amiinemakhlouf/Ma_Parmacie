package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.databinding.DaysReminderItemBinding



class AddReminderDaysAdapter(
     val myDataList: List<String>,
     val dayListener:DayListener
     ) : RecyclerView.Adapter<AddReminderDaysAdapter.ViewHolder>() {
    class ViewHolder(
        val binding: DaysReminderItemBinding
    ) :RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddReminderDaysAdapter.ViewHolder {
        return AddReminderDaysAdapter.ViewHolder(DaysReminderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun getItemCount(): Int {
        return  7
    }

    override fun onBindViewHolder(holder: AddReminderDaysAdapter.ViewHolder, position: Int) {
        val context=holder.itemView.context
        holder.binding.textView.setText(myDataList[position])
        holder.binding.root.setOnClickListener {
            dayListener.onDayClick(position)
        }
    }
    interface DayListener {
        fun onDayClick(position: Int)

    }





}

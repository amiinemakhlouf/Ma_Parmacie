package esprims.gi2.ma_pharmacie.presentation.reminder.show_reminder.model

import android.content.res.Resources.getSystem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.DaysItemsBinding

class DaysAdapter(
    private  val dataset:List<Date>
) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {
    class ViewHolder(
        val binding:DaysItemsBinding
    ) :RecyclerView.ViewHolder(binding.root)
    override fun getItemCount(): Int {
        return  dataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return  ViewHolder(
            DaysItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem=dataset[position]
        holder.binding.txtDate.text=dataset[position].dayInDigits.toString()
        holder.binding.txtDay.text=dataset[position].dayInThreeLetter.toString()
        if(position==2)
        {

            val context=holder.itemView.context
            ((holder.binding.root) as MaterialCardView).elevation= 40F
            holder.binding.txtDate.setTextColor(context.resources.getColor(R.color.dark_green))
            holder.binding.txtDay.setTextColor(context.resources.getColor(R.color.dark_green))
            holder.binding.calendarLinearLayout.background=context.getDrawable(R.drawable.current_day)


        }
    }
    val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()

}

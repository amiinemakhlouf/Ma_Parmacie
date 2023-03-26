package esprims.gi2.ma_pharmacie.presentation.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.DaysItemsBinding
import esprims.gi2.ma_pharmacie.presentation.reminder.model.Date

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
        holder.binding.txtDate.text="15"
        if(position==2)
        {

            val context=holder.itemView.context
            ((holder.binding.root) as MaterialCardView).elevation= 40F
            holder.binding.txtDate.setTextColor(context.resources.getColor(R.color.dark_green))
            holder.binding.txtDay.setTextColor(context.resources.getColor(R.color.dark_green))
            holder.binding.calendarLinearLayout.background=context.getDrawable(R.drawable.rounded_corner)
            holder.binding.txtDate.text="TODAY"
            holder.binding.txtDay.text=""
            holder.binding.txtDate.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(0,80,0,0)
            }

        }
    }
}

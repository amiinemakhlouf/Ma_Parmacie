package esprims.gi2.ma_pharmacie.presentation.notification

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.databinding.DaysItemsBinding
import esprims.gi2.ma_pharmacie.presentation.notification.model.Date

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
        holder.binding.txtDate.text=currentItem.dayInThreeLetter
        if(position==5)
        {
        }
    }
}

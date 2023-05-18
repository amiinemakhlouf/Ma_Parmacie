package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.databinding.TypeItemBinding

class AddReminderAdapter
    (private val dataset:List<Int>,
    private val dayListener: OnTypeListener
    )

    :RecyclerView.Adapter<AddReminderAdapter.ViewHolder>() {
    class ViewHolder(
        val binding: TypeItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return 4
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddReminderAdapter.ViewHolder {

        return AddReminderAdapter.ViewHolder(
            TypeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: AddReminderAdapter.ViewHolder, position: Int) {
        val context=holder.itemView.context
       holder.binding.myImageView.setImageDrawable(context.getDrawable(dataset[position]))
      holder.binding.myImageView.setOnClickListener {
          dayListener.onTypeClick(position)
      }
        if(position==0){
            holder.binding.myImageView.setBackgroundColor(
                context.resources.getColor(
                    R.color.dark_green,
                    null
                ))

        }






    }
    public  interface OnTypeListener{
      fun onTypeClick(position: Int)

    }
}
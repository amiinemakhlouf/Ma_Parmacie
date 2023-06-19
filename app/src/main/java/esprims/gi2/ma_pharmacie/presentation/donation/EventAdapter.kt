package esprims.gi2.ma_pharmacie.presentation.donation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.entity.Event
import esprims.gi2.ma_pharmacie.databinding.DonationItemBinding
import esprims.gi2.ma_pharmacie.databinding.EventItemBinding
import esprims.gi2.ma_pharmacie.presentation.donation.show_donation.DonationAdapter


class EventAdapter (
    val events:List<Event>
        ) : RecyclerView.Adapter<EventAdapter.ViewpagerViewHolder>() {
    class ViewpagerViewHolder (val item:EventItemBinding) :RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewpagerViewHolder {
        return (

            EventAdapter.ViewpagerViewHolder(
                    EventItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
                )
    }

    override fun getItemCount(): Int {
       return events.size
    }

    override fun onBindViewHolder(holder: ViewpagerViewHolder, position: Int) {
        val context=holder.item.root.context
        val actualItem=events[position]
        holder.item.title.setText(actualItem.title)
        holder.item.contactUS.setText(actualItem.contact)
        holder.item.description.setText(actualItem.description)
        holder.item.date.setText(actualItem.date)


    }


}
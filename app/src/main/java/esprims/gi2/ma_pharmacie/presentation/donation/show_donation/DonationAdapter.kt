package esprims.gi2.ma_pharmacie.presentation.donation.show_donation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import esprims.gi2.ma_pharmacie.data.entity.Donation
import esprims.gi2.ma_pharmacie.databinding.DonationItemBinding
import esprims.gi2.ma_pharmacie.presentation.donation.MedicationAdapter

class DonationAdapter(
    val dataset: List<Donation>,
     val donationAdapterListener: DonationAdapterListener) :

    RecyclerView.Adapter<DonationAdapter.ViewHolder>() {
    class ViewHolder (
        val binding: DonationItemBinding
    ):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       return (
                ViewHolder(
                    DonationItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
                )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actualItem=dataset[position]
        holder.binding.name.setText(actualItem.medicationName)
        holder.binding.unit.setText(actualItem.quantity)
        holder.binding.cityValue.setText(actualItem.city)

        holder.binding.root.setOnClickListener {
            donationAdapterListener.onclick(donation = actualItem)
        }
    }

    override fun getItemCount(): Int {
      return   dataset.size
    }



}
interface DonationAdapterListener{

     fun  onclick(donation: Donation)
}


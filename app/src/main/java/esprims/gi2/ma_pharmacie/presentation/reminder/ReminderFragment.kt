package esprims.gi2.ma_pharmacie.presentation.reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import esprims.gi2.ma_pharmacie.databinding.FragmentReminderBinding
import esprims.gi2.ma_pharmacie.presentation.reminder.model.Date
import esprims.gi2.ma_pharmacie.presentation.reminder.model.Reminder


class ReminderFragment : Fragment() {
     private  lateinit var binding:FragmentReminderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentReminderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dates= listOf<Date>(Date("mon",22,1),
            Date("mon",22,1),
            Date("mon",22,1),
            Date("mon",22,1),Date("mon",22,1)
        )
        val reminders = listOf<Reminder>(
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),
            Reminder("dazda",5,"ddzaz","dsqdqdsq"),

        )
        showDaysRecyclerView(dates)
        showReminderRecyclerView( reminders)

    }
    private fun showDaysRecyclerView( dates:List<Date>)
    {
        val daysAdapter=DaysAdapter(dates)
        binding.daysRecyclerView.apply {
            layoutManager= LinearLayoutManager(requireActivity()
                ,LinearLayoutManager.HORIZONTAL,false)
            adapter=daysAdapter
        }
    }

    private  fun showReminderRecyclerView(list:List<Reminder>)
    {
        val reminderAdapter= ReminderAdapter(list)
        binding.reminderRecyclerView.apply {
            layoutManager=LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
            adapter=reminderAdapter
        }
    }
}


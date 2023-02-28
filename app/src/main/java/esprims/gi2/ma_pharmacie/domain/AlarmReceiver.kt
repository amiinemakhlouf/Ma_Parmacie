package esprims.gi2.ma_pharmacie.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import esprims.gi2.ma_pharmacie.presentation.AlarmActivity

class AlarmReceiver  : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
       Intent(context,AlarmActivity::class.java).apply {
           Toast.makeText(context,"hello papa ",Toast.LENGTH_SHORT).show()

           addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
           context!!.startActivity(this)

       }
    }
}
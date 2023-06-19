package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext

class AlertReceiver():BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val time=intent!!.getStringExtra("time")
        Log.d("AlertReceiver",time!!)
        NotificationHelper.createNotification(context!!,null)

    }


}



package esprims.gi2.ma_pharmacie.presentation.reminder.add_reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext

class AlertReceiver():BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val description=intent!!.getStringExtra("description")
        Log.d("AlertReceiver",description!!)
        NotificationHelper.createNotification(context!!,null, description = description)

    }


}



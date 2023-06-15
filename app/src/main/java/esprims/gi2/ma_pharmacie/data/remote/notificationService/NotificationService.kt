package esprims.gi2.ma_pharmacie.data.remote.notificationService

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService


class NotificationService :FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("NotificationService ","  "+token)

    }
}
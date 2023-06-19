import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.app.MyPharmacyApplication
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import java.io.File


object NotificationHelper {
    private const val CHANNEL_ID = "your_channel_id"
    private const val CHANNEL_NAME = "Your Channel Name"
    private const val NOTIFICATION_ID = 1

    fun createNotification(context: Context,file: File?) {
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Prise de médicament")
            .setContentText("Vous avez 2 prise de efferalgan a prendre")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )
            .setLargeIcon(MyPharmacyApplication.instance.applicationContext.resources.getDrawable(
                R.drawable.injection).toBitmap()
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val pendingIntent = createPendingIntent(context)
        builder.setContentIntent(pendingIntent)

        showNotification(context, builder)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,

                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager =
                context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
             PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification(
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

}

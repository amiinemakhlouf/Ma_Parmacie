package esprims.gi2.ma_pharmacie.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.view.WindowManager
import androidx.core.app.AlarmManagerCompat
import esprims.gi2.ma_pharmacie.presentation.main.MainActivity

class ScheduleNotificationUseCase {

    operator fun invoke(activity: MainActivity) {

        if(!Settings.canDrawOverlays(activity))
        {
            activity.startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))

        }
        val intent = Intent(activity, MainActivity::class.java).addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT +
                Intent.FLAG_GRANT_PREFIX_URI_PERMISSION +
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        val alarmManager = activity.getSystemService(AlarmManager::class.java)
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + (1000 *5 ),
            PendingIntent.getActivity(
                activity,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            )

        )


    }
}
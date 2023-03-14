package esprims.gi2.ma_pharmacie.presentation.medication.add_medication

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.WindowManager
import java.text.SimpleDateFormat
import java.util.*

  fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy/MM/dd")
    return format.format(date)
}
 fun getDeviceHeightInDp(context: Context):Int
 {

     val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
     val display: Display = w.defaultDisplay
     val matrix = DisplayMetrics()
     display.getMetrics(matrix)
     val heightInPx=matrix.heightPixels
     return convertPxToDp(heightInPx)

 }

fun convertPxToDp(px:Int):Int
{
    return (px / Resources.getSystem().getDisplayMetrics().density).toInt()

}

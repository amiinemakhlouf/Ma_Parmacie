package esprims.gi2.ma_pharmacie.presentation.pharmacie

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import esprims.gi2.ma_pharmacie.app.MyPharmacyApplication


fun writeOnDrawable(drawableId: Int, text: String): BitmapDrawable {
    val bm: Bitmap =
        BitmapFactory.decodeResource(MyPharmacyApplication.instance.resources, drawableId).copy(Bitmap.Config.ARGB_8888, true)
    val paint = Paint()
    paint.setStyle(Paint.Style.FILL)
    paint.setColor(Color.BLACK)
    paint.setTextSize(20F)
    val canvas = Canvas(bm)
    canvas.drawText(text, 0F, (bm.height / 2).toFloat(), paint)
    return BitmapDrawable(MyPharmacyApplication.instance.resources,bm)
}

package esprims.gi2.ma_pharmacie.presentation.pharmacie

import android.util.Log
import android.view.ScaleGestureDetector

class MyPinchListener
    (val pinchSaceListenner:PinchSaceListenner )
    : ScaleGestureDetector.SimpleOnScaleGestureListener() {

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scaleFactor = detector.scaleFactor
        if(scaleFactor==0.toFloat()){
            return true
        }
        if (scaleFactor >1) {
            Log.d("baba",scaleFactor.toString())
            pinchSaceListenner.zoomOut()
        } else {
            Log.d("baba",scaleFactor.toString())
            pinchSaceListenner.zoomIn()
        }
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        return
    }
}

interface PinchSaceListenner {

    fun zoomIn():Unit
    fun zoomOut():Unit

}

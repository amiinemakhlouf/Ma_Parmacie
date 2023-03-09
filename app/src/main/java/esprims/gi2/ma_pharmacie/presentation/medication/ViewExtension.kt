package esprims.gi2.ma_pharmacie.presentation.medication
import android.app.Activity
import android.view.View
import android.view.animation.Animation

fun View.startAnimation(animation: Animation, callback:() -> Unit )
{
    animation.setAnimationListener(object :Animation.AnimationListener{

        override fun onAnimationStart(animation: Animation?) {
            (  context as Activity ) .setTitle("")
        }
        override fun onAnimationEnd(animation: Animation?) {
            callback()
        }

        override fun onAnimationRepeat(animation: Animation?) =Unit

    })
    startAnimation(animation)
}
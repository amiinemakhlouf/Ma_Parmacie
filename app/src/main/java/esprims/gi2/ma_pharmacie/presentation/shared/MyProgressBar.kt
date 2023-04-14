package esprims.gi2.ma_pharmacie.presentation.shared

import android.content.Context
import androidx.appcompat.app.AlertDialog
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.app.MyPharmacyApplication

object MyProgressBar {


   fun  show(context: Context){
      var  alertDialogBuilder= AlertDialog.Builder(context)
           .setView(R.layout.custom_progress_bar).show()
         alertDialogBuilder.show()
    }
    fun hide(context: Context) {
       var  alertDialogBuilder= AlertDialog.Builder(context)
            .setView(R.layout.custom_progress_bar).show()
        alertDialogBuilder.hide()
    }
}
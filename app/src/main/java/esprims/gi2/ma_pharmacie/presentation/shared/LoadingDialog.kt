package esprims.gi2.ma_pharmacie.presentation.shared

import android.app.Activity
import android.app.AlertDialog
import esprims.gi2.ma_pharmacie.R

class LoadingDialog(
   val activity:Activity,
) {
    private var dialog:AlertDialog?=null

    init {
        val builder= AlertDialog.Builder(activity,R.style.CustomAlertDialogTheme)
        builder.setView(R.layout.custom_progress_bar)
        dialog=builder.create()
        dialog!!.show()
        dialog!!.setCancelable(false)

    }

    fun showDialog(){

        dialog!!.show()

    }
    fun hideDialog()
    {
        dialog!!.hide()
    }
}
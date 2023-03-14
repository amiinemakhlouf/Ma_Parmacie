package esprims.gi2.ma_pharmacie.presentation.medication.add_medication

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import esprims.gi2.ma_pharmacie.R

class AddMedicationValidator(){

    companion object{
        fun isTextInputValid(inputs:TextInputLayout, context:AppCompatActivity):Boolean{

            var isValid:Boolean=true
            if(inputs.editText?.text.isNullOrBlank()){
                if(inputs.id== R.id.add_descriptionEt)
                {
                    val description_et= context .findViewById<TextView>(R.id.description_error_message)
                    description_et.visibility= View.VISIBLE
                }
                inputs.error="ce champs est vide"
                isValid=false

            }


           inputs.editText?.setOnFocusChangeListener { v, hasFocus ->
                if(inputs.id==R.id.add_descriptionEt)
                {
                    val description_et= context .findViewById<TextView>(R.id.description_error_message)
                    description_et.visibility= View.INVISIBLE

                }
                if(hasFocus){
                    inputs.error=null
                    inputs.helperText=null
                    inputs.isErrorEnabled=false
                }


            }
            return isValid

        }

    }


}
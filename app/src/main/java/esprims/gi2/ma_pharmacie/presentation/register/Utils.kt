package esprims.gi2.ma_pharmacie.presentation.register

import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

object Utils {

    fun isPasswordMatches(password:String, confirmPassword:String) =password==confirmPassword

    fun isInputEmpty(textInputLayout: TextInputLayout)= textInputLayout.editText?.editableText.isNullOrBlank()
    fun isPasswordValid(password: String)=password.length>=8

    fun isEmailValid(email:String)= !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

}
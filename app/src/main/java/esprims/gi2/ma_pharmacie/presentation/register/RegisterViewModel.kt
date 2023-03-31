package esprims.gi2.ma_pharmacie.presentation.register

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.ViewModel
import esprims.gi2.ma_pharmacie.domain.RegisterUseCase
import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.Result.Success
import esprims.gi2.ma_pharmacie.Result

class RegisterViewModel(
  
) :ViewModel() {
     private lateinit var registerUseCase: RegisterUseCase
         

    suspend fun register(registerDto: RegisterDto):Result<Any>{

       return registerUseCase.invoke(registerDto)


    }


    fun isPasswordMatches(password:String, confirmPassword:String) =password==confirmPassword



}
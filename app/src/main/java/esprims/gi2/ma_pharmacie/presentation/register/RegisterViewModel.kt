package esprims.gi2.ma_pharmacie.presentation.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.sendOtpConfirmationUseCase
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.Result
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val   sendOtpConfirmationUseCase: sendOtpConfirmationUseCase
) :ViewModel() {

    suspend fun register(registerDto: RegisterDto):Result<Any>{

       return sendOtpConfirmationUseCase.invoke(registerDto)


    }


    fun isPasswordMatches(password:String, confirmPassword:String) =password==confirmPassword



}
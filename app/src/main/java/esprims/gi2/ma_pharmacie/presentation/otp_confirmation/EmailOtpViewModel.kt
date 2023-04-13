package esprims.gi2.ma_pharmacie.presentation.otp_confirmation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.ConfirmEmailUseCase
import esprims.gi2.ma_pharmacie.dto.ConfirmDto
import esprims.gi2.ma_pharmacie.Result
import esprims.gi2.ma_pharmacie.dto.ForgetPasswordDto
import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.useCase.ForgetPasswordUseCase
import esprims.gi2.ma_pharmacie.useCase.sendOtpConfirmationUseCase
import javax.inject.Inject

@HiltViewModel
class EmailOtpViewModel @Inject constructor(
    val confirmEmailUseCase: ConfirmEmailUseCase,
    val sendOtpUseCaseForRegister:sendOtpConfirmationUseCase,
    val sendOtpUseCaseForForgetPassword: ForgetPasswordUseCase

) :ViewModel() {

    suspend fun confirmEmail(confirmDto: ConfirmDto,forRegister:Boolean):Result<String>{

        val result=confirmEmailUseCase.confirmEmail(confirmDto,forRegister)
       when(result)
       {
           is Result.Success  -> return  Result.Success(message = (result as Result.Success).message)

           else -> return  Result.Error("resend code")

       }

    }
    suspend fun  resendOtpCode(registerDto:RegisterDto){

       val result= sendOtpUseCaseForRegister.invoke(registerDto)



    }

   suspend fun resendOtpCodeForForgetPassword(email:String) {


        sendOtpUseCaseForForgetPassword(ForgetPasswordDto(email))


    }


}
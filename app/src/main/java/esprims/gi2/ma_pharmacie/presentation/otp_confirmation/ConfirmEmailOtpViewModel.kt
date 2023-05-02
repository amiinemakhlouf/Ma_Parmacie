package esprims.gi2.ma_pharmacie.presentation.otp_confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.authentication.ConfirmEmailUseCase
import esprims.gi2.ma_pharmacie.dto.ConfirmDto
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.dto.ForgetPasswordDto
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.useCase.authentication.ForgetPasswordUseCase
import esprims.gi2.ma_pharmacie.useCase.authentication.SendOtpConfirmationUseCase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmEmailOtpViewModel @Inject constructor(
    val confirmEmailUseCase: ConfirmEmailUseCase,
    val sendOtpUseCaseForRegister: SendOtpConfirmationUseCase,
    val sendOtpUseCaseForForgetPassword: ForgetPasswordUseCase

) :ViewModel() {
    private val emailOtpStateFlow=MutableStateFlow<UIState<String>>(UIState.Default)
    val _emailStateFlow=emailOtpStateFlow
    private  val resendCodeStateFlow= MutableStateFlow<UIState<String>>(UIState.Default)
    val _resendCodeStateFlow=resendCodeStateFlow

    suspend fun confirmEmail(confirmDto: ConfirmDto,forRegister:Boolean){

        val result=confirmEmailUseCase.confirmEmail(confirmDto,forRegister)
        emailOtpStateFlow.emit(UIState.Loading())
       when(result)
       {
           is Result.Success  ->  {
               emailOtpStateFlow.emit(UIState.Success(result.message))

           }

           else ->  {
             emailOtpStateFlow.emit(UIState.Error())
           }

       }

    }
     fun  resendOtpCode(registerDto:RegisterDto){

        viewModelScope.launch(IO){


       val result= sendOtpUseCaseForRegister.invoke(registerDto)

        when(result)
        {
            is Result.Success ->  resendCodeStateFlow.emit(UIState.Success())
            is Result.Error ->  resendCodeStateFlow.emit(UIState.Error())
        }
        }



    }

    fun resendOtpCodeForForgetPassword(email:String) {

        viewModelScope.launch (IO){


        val result=sendOtpUseCaseForForgetPassword.invoke(ForgetPasswordDto(email))

       when(result)
       {
           is Result.Success -> resendCodeStateFlow.emit(UIState.Success())
           is Result.Error ->  resendCodeStateFlow.emit(UIState.Error())
       }
        }

    }


}
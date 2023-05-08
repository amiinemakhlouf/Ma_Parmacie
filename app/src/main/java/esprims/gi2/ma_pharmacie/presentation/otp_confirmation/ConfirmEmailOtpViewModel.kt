package esprims.gi2.ma_pharmacie.presentation.otp_confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.authentication.ConfirmEmailUseCase
import esprims.gi2.ma_pharmacie.requestModel.ConfirmRequestModel
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.requestModel.ForgetPasswordRequestModel
import esprims.gi2.ma_pharmacie.requestModel.RegisterRequestModel
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

    suspend fun confirmEmail(confirmRequestModel: ConfirmRequestModel, forRegister:Boolean){

        val result=confirmEmailUseCase.confirmEmail(confirmRequestModel,forRegister)
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
     fun  resendOtpCode(registerRequestModel:RegisterRequestModel){

        viewModelScope.launch(IO){


       val result= sendOtpUseCaseForRegister.invoke(registerRequestModel)

        when(result)
        {
            is Result.Success ->  resendCodeStateFlow.emit(UIState.Success())
            is Result.Error ->  resendCodeStateFlow.emit(UIState.Error())
        }
        }



    }

    fun resendOtpCodeForForgetPassword(email:String) {

        viewModelScope.launch (IO){


        val result=sendOtpUseCaseForForgetPassword.invoke(ForgetPasswordRequestModel(email))

       when(result)
       {
           is Result.Success -> resendCodeStateFlow.emit(UIState.Success())
           is Result.Error ->  resendCodeStateFlow.emit(UIState.Error())
       }
        }

    }


}
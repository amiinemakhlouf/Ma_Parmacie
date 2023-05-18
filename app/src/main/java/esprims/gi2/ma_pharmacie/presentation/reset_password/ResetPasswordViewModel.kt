package esprims.gi2.ma_pharmacie.presentation.reset_password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.authentication.ResetPasswordUseCase
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result

@HiltViewModel
class ResetPasswordViewModel @Inject constructor( private val resetPasswordUseCase: ResetPasswordUseCase): ViewModel()

{
    suspend fun  resetPassword(loginRequestModel: LoginRequestModel) : Result<String> {
      val result=  resetPasswordUseCase.invoke(loginRequestModel)

        return when(result) {
            is Result.Success -> Result.Success("updated succesfully")
            else -> Result.Error("error occured")
        }
    }
}
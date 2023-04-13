package esprims.gi2.ma_pharmacie.presentation.reset_password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.ResetPasswordUseCase
import esprims.gi2.ma_pharmacie.dto.LoginDto
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.Result

@HiltViewModel
class ResetPasswordViewModel @Inject constructor( private val resetPasswordUseCase:ResetPasswordUseCase): ViewModel()

{
    suspend fun  resetPassword(loginDto: LoginDto) :Result<String>{
      val result=  resetPasswordUseCase.invoke(loginDto)

        return when(result) {
            is Result.Success -> Result.Success("updated succesfully")
            else -> Result.Error("error occured")
        }
    }
}
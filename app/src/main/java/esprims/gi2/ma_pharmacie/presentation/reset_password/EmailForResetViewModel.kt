package esprims.gi2.ma_pharmacie.presentation.reset_password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.ForgetPasswordUseCase
import esprims.gi2.ma_pharmacie.dto.ForgetPasswordDto
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result

@HiltViewModel
class EmailForResetViewModel @Inject constructor( private val useCase: ForgetPasswordUseCase)  :ViewModel() {

    suspend fun sendEmailToGetOtpCode(forgetPasswordDto: ForgetPasswordDto): Result<String> {

        val result = useCase.invoke(forgetPasswordDto)

        when (result) {
            is Result.Success -> return Result.Success()
            else -> return  Result.Error("aucun e-mail associée au compte.")
        }


    }
}
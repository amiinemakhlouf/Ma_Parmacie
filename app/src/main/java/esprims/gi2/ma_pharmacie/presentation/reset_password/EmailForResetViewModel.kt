package esprims.gi2.ma_pharmacie.presentation.reset_password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.authentication.ForgetPasswordUseCase
import esprims.gi2.ma_pharmacie.requestModel.ForgetPasswordRequestModel
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result

@HiltViewModel
class EmailForResetViewModel @Inject constructor( private val useCase: ForgetPasswordUseCase)  :ViewModel() {

    suspend fun sendEmailToGetOtpCode(forgetPasswordRequestModel: ForgetPasswordRequestModel): Result<String> {

        val result = useCase.invoke(forgetPasswordRequestModel)

        when (result) {
            is Result.Success -> return Result.Success()
            else -> return  Result.Error("aucun e-mail associée au compte.")
        }


    }
}
package esprims.gi2.ma_pharmacie.useCase.authentication

import esprims.gi2.ma_pharmacie.data.repository.UserRepository
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {


    suspend operator fun invoke(loginRequestModel: LoginRequestModel): Result<String> {
        try {
            val result = userRepository.resetPassword(loginRequestModel)
            return result

        } catch (e: Exception) {
            return Result.Error("pas de connexion internet")

        }

    }




}
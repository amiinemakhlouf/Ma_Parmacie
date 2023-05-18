package esprims.gi2.ma_pharmacie.useCase.authentication

import esprims.gi2.ma_pharmacie.data.repository.UserRepository
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun loginWithEmailAndPassword(loginRequestModel: LoginRequestModel): Result<String> {

        try {
            val result = userRepository.loginWithEmailAndPassword(loginRequestModel)
            return result
        } catch (e: Exception) {
            return Result.Error("pas de connexion internet")

        }


    }
}
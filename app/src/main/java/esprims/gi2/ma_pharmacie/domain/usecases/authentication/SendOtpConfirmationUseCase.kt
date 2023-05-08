package esprims.gi2.ma_pharmacie.useCase.authentication

import esprims.gi2.ma_pharmacie.data.repository.UserRepository

import esprims.gi2.ma_pharmacie.requestModel.RegisterRequestModel
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject


class SendOtpConfirmationUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(registerRequestModel: RegisterRequestModel): Result<String>
    {

           try {
               val result=userRepository.sendOtpConfirmation(registerRequestModel)
               return  result
           }catch (e: Exception) {
               return Result.Error("pas de connexion internet")

           }


    }
}
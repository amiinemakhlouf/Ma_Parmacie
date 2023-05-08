package esprims.gi2.ma_pharmacie.useCase.authentication

import esprims.gi2.ma_pharmacie.data.repository.UserRepository
import esprims.gi2.ma_pharmacie.requestModel.ConfirmRequestModel
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class ConfirmEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {


    suspend fun confirmEmail(confirmRequestModel: ConfirmRequestModel, forRegister:Boolean,

                             ) : Result<String>
    {
        try {
          val response=  userRepository.confirmEmail(confirmRequestModel,forRegister)
            return  response
        }
        catch (e:Exception)
        {
            return Result.Error("pas de connexion")
        }

    }


}

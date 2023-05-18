package esprims.gi2.ma_pharmacie.useCase.authentication

import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.data.repository.UserRepository
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class LoginGoogleClient @Inject constructor(
    private  val userRepository: UserRepository
) {

    suspend operator fun invoke(user:User): Result<String>
    {

        try {
            return  userRepository.loginWithGoogleClient(user)
        }
        catch (e:Exception)
        {
            return Result.Error("pas de connexion internet")

        }


    }
}
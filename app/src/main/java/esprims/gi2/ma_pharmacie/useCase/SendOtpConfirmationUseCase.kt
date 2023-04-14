package esprims.gi2.ma_pharmacie.useCase

import esprims.gi2.ma_pharmacie.data.remote.userService.UserService

import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject


class SendOtpConfirmationUseCase @Inject constructor(private val userApi:UserService) {

    suspend operator fun invoke(registerDto: RegisterDto): Result<String>
    {

           try {


               val result = userApi.checkEmailAvailable(registerDto)

               if (result.code() < 300) {
                   return Result.Success(result.body().toString())
               }
               val emailAlreadyUsed="E-mail indisponible."
               return Result.Error(message =emailAlreadyUsed )
           }
           catch (e:java.lang.Exception)
           {
               val noInternetConnectionMessage="Pas de connexion internet"
               return Result.Error(message =noInternetConnectionMessage )

           }




    }
}
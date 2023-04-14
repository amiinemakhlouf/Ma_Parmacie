package esprims.gi2.ma_pharmacie.useCase

import android.util.Log
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService

import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.presentation.shared.Result


class sendOtpConfirmationUseCase {

    suspend operator fun invoke(registerDto: RegisterDto): Result<String>
    {
        val userApi= RetrofitBuilder.build().create(UserService::class.java)

           try {


               val result = userApi.checkEmailAvailable(registerDto)

               if (result.code() < 300) {
                   return Result.Success(result.body().toString())
               }
               return Result.Error(message = "E-mail indisponible.")
           }
           catch (e:java.lang.Exception)
           {
               return Result.Error(message = "Pas de connexion internet")

           }




    }
}
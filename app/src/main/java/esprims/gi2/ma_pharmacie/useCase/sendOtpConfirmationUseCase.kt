package esprims.gi2.ma_pharmacie.useCase

import android.util.Log
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService

import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.Result


class sendOtpConfirmationUseCase {

    suspend operator fun invoke(registerDto: RegisterDto):Result<String>
    {
        val userApi= RetrofitBuilder.build().create(UserService::class.java)

            val result= userApi.checkEmailAvailable(registerDto)
            Log.d("my result",result.body().toString())

            if (result.code()<300)
            {
                Log.d("my result","i'm here bro")
                return Result.Success(result.body().toString())
            }
            Log.d("my rest","i return error")
            return Result.Error("email already exist")




    }
}
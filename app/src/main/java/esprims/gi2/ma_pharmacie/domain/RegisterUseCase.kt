package esprims.gi2.ma_pharmacie.domain

import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import  esprims.gi2.ma_pharmacie.Result
import java.lang.Exception

import esprims.gi2.ma_pharmacie.dto.RegisterDto
import retrofit2.Retrofit


class RegisterUseCase {

    suspend operator fun invoke(registerDto: RegisterDto):Result<Any>
    {
        val userApi= RetrofitBuilder.build().create(UserService::class.java)

            val result= userApi.register(registerDto)

            if (result.isSuccessful)
            {
                return Result.Success("")
            }
            val errorMessage=result.errorBody().toString()
            return Result.Error(errorMessage)




    }
}
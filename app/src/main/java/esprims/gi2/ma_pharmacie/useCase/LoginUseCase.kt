package esprims.gi2.ma_pharmacie.useCase

import android.util.Log
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.dto.LoginDto
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userApi: UserService) {

     suspend fun loginWithEmailAndPassword(loginDto:LoginDto): Result<String>
    {

        try {


        val result=userApi.login(loginDto)

        if (result.isSuccessful)
        {
            val jwt=result.headers().get("Authorization")!!

            return  Result.Success(data = jwt)
        }


        val errorMessage=result.errorBody()!!.string()
            return Result.Error(message = errorMessage)
        }
        catch (e:java.lang.Exception){
            val noInternetMEssage="Pas de connexion internet"
           return  Result.Error(noInternetMEssage)
        }


    }



}
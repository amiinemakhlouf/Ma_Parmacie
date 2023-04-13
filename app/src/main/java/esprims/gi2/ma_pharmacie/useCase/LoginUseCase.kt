package esprims.gi2.ma_pharmacie.useCase

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import esprims.gi2.ma_pharmacie.R
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.Result
import esprims.gi2.ma_pharmacie.app.MyPharmacyApplication
import esprims.gi2.ma_pharmacie.dto.LoginDto

class LoginUseCase {

     suspend fun loginWithEmailAndPassword(loginDto:LoginDto):Result<String>
    {
        val userApi= RetrofitBuilder.build().create(UserService::class.java)


        val result=userApi.login(loginDto)

        Log.d("login use case",result.code().toString())
        if (result.isSuccessful)
        {
            val jwt=result.headers().get("Authorization")!!

            return  Result.Success(data = jwt)
        }


        val errorMessage=result.errorBody()!!.string()
        Log.d("login use case",errorMessage)
        Log.d("papi",errorMessage)
        return Result.Error(message = errorMessage)
    }



}
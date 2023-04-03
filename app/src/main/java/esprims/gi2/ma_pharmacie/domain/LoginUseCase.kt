package esprims.gi2.ma_pharmacie.domain

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
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
            return  Result.Success()
        }


        val errorMessage=result.errorBody()!!.string()
        Log.d("login use case",errorMessage)
        Log.d("papi",errorMessage)
        return Result.Error(message = errorMessage)
    }

    fun loginWithGoogle():Unit {

        val context = MyPharmacyApplication.instance
        val options =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestIdToken(
                    context.getString(
                        R.string.default_web_client_id
                    )
                ).build()
        val signInClient = GoogleSignIn.getClient(context, options)

    }

}
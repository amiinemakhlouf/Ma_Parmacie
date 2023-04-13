package esprims.gi2.ma_pharmacie.useCase

import android.util.Log
import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.Result

class LoginGoogleClient {

    suspend operator fun invoke(user:User):Result<String>
    {
        val userApi= RetrofitBuilder.build().create(UserService::class.java)
       val result= userApi.registerGoogleClient(user)
        Log.d("code",result.code().toString())
       return  when(result.isSuccessful)
        {
            true ->  {
                val  jwt=result.headers().get("Authorization")!!
                Result.Success(data=jwt )
            }
            else -> {throw error(result.errorBody()!!.string())
            }

        }


    }
}
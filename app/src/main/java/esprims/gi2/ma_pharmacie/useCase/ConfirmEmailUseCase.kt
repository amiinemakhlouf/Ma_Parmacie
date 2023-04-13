package esprims.gi2.ma_pharmacie.useCase

import android.util.Log
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.dto.ConfirmDto
import esprims.gi2.ma_pharmacie.Result
import retrofit2.Response

class ConfirmEmailUseCase {


    suspend fun confirmEmail(confirmDto: ConfirmDto,forRegister:Boolean) :Result<String>
    {
        val userApi= RetrofitBuilder.build().create(UserService::class.java)
        var jwt:String=""
        try {


        var result:Response<out  Any> = if(forRegister){
            Log.d("ConfirmEmailUseCase","register bro")
            val response= userApi.sentOtpToConfirmEmail(confirmDto)
            Log.d("coode",response.code().toString())
            if (response.isSuccessful)
            {
                jwt=response.headers().get("Authorization")!!
                Log.d("dodo",jwt.toString())

            }

            response


        } else{
            Log.d("ConfirmEmailUseCase","reset bro")
            userApi.confirmOtp(confirmDto)
        }

        Log.d("confrim status code",result.code().toString())
        if(result.isSuccessful){
            return Result.Success(message =jwt )
        }
        }catch (e:Exception)
        {
            return  esprims.gi2.ma_pharmacie.Result.Error("no internet connection")
        }
        return Result.Error("wrong code")
    }
}
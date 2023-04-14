package esprims.gi2.ma_pharmacie.useCase

import android.util.Log
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.dto.ConfirmDto
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import retrofit2.Response
import javax.inject.Inject

class ConfirmEmailUseCase @Inject constructor(
    private val userApi:UserService
) {


    suspend fun confirmEmail(confirmDto: ConfirmDto,forRegister:Boolean,

    ) : Result<String>
    {
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
            return  Result.Error("no internet connection")
        }
        return Result.Error("wrong code")
    }
}
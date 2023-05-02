package esprims.gi2.ma_pharmacie.useCase.authentication

import android.util.Log
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
            val response= userApi.sentOtpToConfirmEmail(confirmDto)
            if (response.isSuccessful)
            {
                jwt=response.headers().get("Authorization")!!
                Log.d("dodo",jwt.toString())

            }

            response


        } else{
            userApi.confirmOtp(confirmDto)
        }

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
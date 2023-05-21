package esprims.gi2.ma_pharmacie.data.repository

import android.util.Log
import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.requestModel.ConfirmRequestModel
import esprims.gi2.ma_pharmacie.requestModel.ForgetPasswordRequestModel
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import esprims.gi2.ma_pharmacie.requestModel.RegisterRequestModel
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import retrofit2.Response
import javax.inject.Inject

class UserRepository  @Inject constructor(
    private val userApi: UserService

) {


    suspend fun confirmEmail(confirmRequestModel: ConfirmRequestModel, forRegister:Boolean,

                             ) : Result<String>
    {
        var jwt:String=""



            var result: Response<out Any> = if(forRegister){
                val response= userApi.sentOtpToConfirmEmail(confirmRequestModel)
                if (response.isSuccessful)
                {
                    jwt=response.headers().get("Authorization")!!
                    Log.d("dodo",jwt.toString())

                }

                response


            } else{
                userApi.confirmOtp(confirmRequestModel)
            }

            if(result.isSuccessful){
                return Result.Success(message =jwt )
            }
        return Result.Error("wrong code")
    }

    suspend fun  forgetPassword(forgetPasswordRequestModel: ForgetPasswordRequestModel) : Result<String> {


        val result=userApi.sendEmailToGetOtpCode(forgetPasswordRequestModel)

        if(result.isSuccessful){

            return Result.Success("email exist")
        }
        else
            return  Result.Error("email does not exist")
    }

    suspend fun loginWithGoogleClient(user: User): Result<String>
    {



            val result= userApi.registerGoogleClient(user)
            return  when(result.isSuccessful)
            {
                true ->  {
                    val  jwt=result.headers().get("Authorization")!!
                    Result.Success(data=jwt )
                }
                else -> {throw error(message=result.errorBody()!!.string())
                }

            }

    }
    suspend fun loginWithEmailAndPassword(loginRequestModel: LoginRequestModel): Result<String>
    {




            val result=userApi.login(loginRequestModel)

            if (result.isSuccessful)
            {
                val jwt=result.headers().get("Authorization")!!

                return  Result.Success(data = jwt)
            }


            result.errorBody()?.let {
                return Result.Error(message = "Identifiants incorrects")

            }
            return Result.Error(message = "Erreur de serveur")
        }


    suspend fun resetPassword(loginRequestModel: LoginRequestModel): Result<String>
    {
        val result=userApi.resetPassword(loginRequestModel)
        if (result.isSuccessful)
        {
            return  Result.Success("reset correctly")
        }
        return Result.Error("error occured")

    }
    suspend fun  sendOtpConfirmation(registerRequestModel: RegisterRequestModel): Result<String>
    {




            val result = userApi.checkEmailAvailable(registerRequestModel)

            if (result.isSuccessful) {
                return Result.Success(result.body().toString())
            }
            val emailAlreadyUsed="E-mail indisponible."
            return Result.Error(message =emailAlreadyUsed )





    }
}









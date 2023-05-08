package esprims.gi2.ma_pharmacie.data.remote.userService

import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.data.remote.util.SimpleStringResponse
import esprims.gi2.ma_pharmacie.requestModel.ConfirmRequestModel
import esprims.gi2.ma_pharmacie.requestModel.ForgetPasswordRequestModel
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import esprims.gi2.ma_pharmacie.requestModel.RegisterRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UserService {

    @POST("api/register")
    suspend fun checkEmailAvailable(@Body  registerRequestModel: RegisterRequestModel):Response<SimpleStringResponse>

    @POST("api/login")
    suspend fun login(@Body  loginRequestModel: LoginRequestModel): Response<User>

    @POST("api/account/confirmation")
    suspend fun sentOtpToConfirmEmail( @Body  confirmRequestModel: ConfirmRequestModel): Response<User>

    @POST("api/user/reset_password")
    suspend fun sendEmailToGetOtpCode(@Body  forgetPasswordRequestModel: ForgetPasswordRequestModel): Response<SimpleStringResponse>

    @POST("api/usr/otp_check")
    suspend fun  confirmOtp(@Body confirmRequestModel: ConfirmRequestModel) :Response<SimpleStringResponse>

    @POST("api/password/reset")
    suspend fun resetPassword(@Body loginRequestModel: LoginRequestModel):Response<User>

    @POST("apo")
    suspend fun registerGoogleClient(@Body user:User) : Response<User>

}

data class OtpCode(val code:String)
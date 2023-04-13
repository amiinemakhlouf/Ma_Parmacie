package esprims.gi2.ma_pharmacie.data.remote.userService

import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.data.remote.util.SimpleStringResponse
import esprims.gi2.ma_pharmacie.dto.ConfirmDto
import esprims.gi2.ma_pharmacie.dto.ForgetPasswordDto
import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface UserService {

    @POST("api/register")
    suspend fun checkEmailAvailable(@Body  registerDto: RegisterDto):Response<SimpleStringResponse>

    @POST("api/login")
    suspend fun login(@Body  loginDto: LoginDto): Response<User>

    @POST("api/account/confirmation")
    suspend fun sentOtpToConfirmEmail( @Body  confirmDto: ConfirmDto): Response<User>

    @POST("api/user/reset_password")
    suspend fun sendEmailToGetOtpCode(@Body  forgetPasswordDto: ForgetPasswordDto): Response<SimpleStringResponse>

    @POST("api/usr/otp_check")
    suspend fun  confirmOtp(@Body confirmDto: ConfirmDto) :Response<SimpleStringResponse>

    @POST("api/password/reset")
    suspend fun resetPassword(@Body loginDto: LoginDto):Response<User>

    @POST("apo")
    suspend fun registerGoogleClient(@Body user:User) : Response<User>

}

data class OtpCode(val code:String)
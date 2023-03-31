package esprims.gi2.ma_pharmacie.data.remote.userService

import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UserService {

    @POST("register")
    suspend fun register(@Body  registerDto: RegisterDto):Response<Any>

}
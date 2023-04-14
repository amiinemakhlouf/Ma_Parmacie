package esprims.gi2.ma_pharmacie.useCase

import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val userApi:UserService
) {


    suspend operator fun invoke(loginDto: LoginDto): Result<String>
    {
        val result=userApi.resetPassword(loginDto)
        if (result.isSuccessful)
        {
            return  Result.Success("reset correctly")
        }
        return Result.Error("error occured")

    }



}
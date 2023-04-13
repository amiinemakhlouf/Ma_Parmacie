package esprims.gi2.ma_pharmacie.useCase
import esprims.gi2.ma_pharmacie.data.remote.RetrofitBuilder
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.dto.ForgetPasswordDto

class ForgetPasswordUseCase  {
    
    // check if email exisits in database , if exists it send otp  to this email 

   suspend operator  fun invoke (forgetPasswordDto: ForgetPasswordDto) :esprims.gi2.ma_pharmacie.Result<String>{

        val userService= RetrofitBuilder.build().create(UserService::class.java)

        val result=userService.sendEmailToGetOtpCode(forgetPasswordDto)

        if(result.isSuccessful){

            return esprims.gi2.ma_pharmacie.Result.Success("email exist")
        }
       else
           return  esprims.gi2.ma_pharmacie.Result.Error("email does not exist")
    }
}
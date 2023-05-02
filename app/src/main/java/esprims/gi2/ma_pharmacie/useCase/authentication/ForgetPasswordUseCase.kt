package esprims.gi2.ma_pharmacie.useCase.authentication
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.dto.ForgetPasswordDto
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(private val userService: UserService)  {
    
    // check if email exisits in database , if exists it send otp  to this email 

   suspend operator  fun invoke (forgetPasswordDto: ForgetPasswordDto) : Result<String> {


        val result=userService.sendEmailToGetOtpCode(forgetPasswordDto)

        if(result.isSuccessful){

            return Result.Success("email exist")
        }
       else
           return  Result.Error("email does not exist")
    }
}
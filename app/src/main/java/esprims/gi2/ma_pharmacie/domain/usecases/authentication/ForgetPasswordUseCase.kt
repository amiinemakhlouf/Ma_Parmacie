package esprims.gi2.ma_pharmacie.useCase.authentication
import esprims.gi2.ma_pharmacie.data.repository.UserRepository
import esprims.gi2.ma_pharmacie.requestModel.ForgetPasswordRequestModel
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(private val userRepository: UserRepository)  {
    
    // check if email exisits in database , if exists it send otp  to this email 

   suspend operator  fun invoke (forgetPasswordRequestModel: ForgetPasswordRequestModel) : Result<String> {

       try {
           return userRepository.forgetPassword(forgetPasswordRequestModel)

       }
       catch (e:Exception)
       {
           return Result.Error("pas de connexion")
       }


   }




}
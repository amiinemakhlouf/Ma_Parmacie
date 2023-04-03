package esprims.gi2.ma_pharmacie.presentation.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.domain.LoginUseCase
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.Result
import esprims.gi2.ma_pharmacie.dto.LoginDto

@HiltViewModel
class LoginViewModel @Inject constructor (
    private  val loginUseCase: LoginUseCase)
    :ViewModel() {

        suspend fun loginWithEmailAndPassword(loginDto:LoginDto):Result<String>{

            val result=loginUseCase.loginWithEmailAndPassword(loginDto)
           when(result)
           {
               is Result.Success -> return Result.Success()

               is Result.Error -> return  Result.Error(result.message)

           }



        }

    fun loginWithGoogle()=loginUseCase.loginWithGoogle()
}
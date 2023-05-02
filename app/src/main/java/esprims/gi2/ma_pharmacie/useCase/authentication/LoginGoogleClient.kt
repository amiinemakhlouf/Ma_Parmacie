package esprims.gi2.ma_pharmacie.useCase.authentication

import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import javax.inject.Inject

class LoginGoogleClient @Inject constructor(
    private  val userApi:UserService
) {

    suspend operator fun invoke(user:User): Result<String>
    {
        try{


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

        }catch (e:Exception)
        {
           return  Result.Error("no internet connection")
        }


    }
}
package esprims.gi2.ma_pharmacie.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.authentication.LoginUseCase
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.requestModel.LoginRequestModel
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.useCase.authentication.LoginGoogleClient
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor (
    private  val loginUseCase: LoginUseCase,
    private val loginGoogleClient: LoginGoogleClient,

    )
    :ViewModel() {

    private val loginState: MutableSharedFlow<UIState<String>> = MutableSharedFlow()
    val _loginState: MutableSharedFlow<UIState<String>> = loginState



    fun loginWithEmailAndPassword(loginRequestModel: LoginRequestModel): Unit {
        viewModelScope.launch(IO) {
            loginState.emit(UIState.Loading)

            val result = loginUseCase.loginWithEmailAndPassword(loginRequestModel)
            when (result) {
                is Result.Success ->{
                    result.data?.let {
                        loginState.emit(UIState.Success<String>(result.data))
                    }
                }

                is Result.Error -> {
                    result.message?.let {
                        loginState.emit(UIState.Error(result.message))

                    }
                }

            }


        }

    }

    suspend fun loginWithGoogleAccount(user: User): Unit {
        viewModelScope.launch(IO) {
            loginState.emit(UIState.Loading)
            val result = loginGoogleClient.invoke(user)
            when (result) {
                is Result.Success -> {
                    val success = result as Result.Success
                    success.data?.let {
                        loginState.emit(UIState.Success(success.data))

                    }
                }
                is Result.Error -> {
                    result.message?.let {
                        loginState.emit(UIState.Error(result.message))

                    }

                }
            }
        }
    }
}





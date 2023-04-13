package esprims.gi2.ma_pharmacie.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.LoginUseCase
import javax.inject.Inject
import esprims.gi2.ma_pharmacie.Result
import esprims.gi2.ma_pharmacie.data.entity.User
import esprims.gi2.ma_pharmacie.dto.LoginDto
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import esprims.gi2.ma_pharmacie.useCase.LoginGoogleClient
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor (
    private  val loginUseCase: LoginUseCase,
    private val loginGoogleClient:LoginGoogleClient,

    )
    :ViewModel() {

    private val loginState: MutableStateFlow<UIState<String>> = MutableStateFlow(UIState.Default)
    val _loginState: MutableStateFlow<UIState<String>> = loginState


    fun loginWithEmailAndPassword(loginDto: LoginDto): Unit {
        viewModelScope.launch(IO) {
            loginState.emit(UIState.Loading())

            val result = loginUseCase.loginWithEmailAndPassword(loginDto)
            when (result) {
                is Result.Success -> loginState.emit(UIState.Success<String>(result.data))

                is Result.Error -> loginState.emit(UIState.Error())

            }


        }

    }

    suspend fun loginWithGoogleAccount(user: User): Unit {
        viewModelScope.launch(IO) {


            val result = loginGoogleClient.invoke(user)
            loginState.emit(UIState.Default)
            when (result) {
                is Result.Success -> {
                    val success = result as Result.Success
                    loginState.emit(UIState.Success(success.data))
                }
                is Result.Error -> {
                    loginState.emit(UIState.Error())

                }
            }
        }
    }
}





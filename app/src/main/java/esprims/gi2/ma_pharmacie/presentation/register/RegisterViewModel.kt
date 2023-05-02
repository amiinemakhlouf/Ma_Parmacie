package esprims.gi2.ma_pharmacie.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import esprims.gi2.ma_pharmacie.useCase.authentication.SendOtpConfirmationUseCase
import esprims.gi2.ma_pharmacie.dto.RegisterDto
import esprims.gi2.ma_pharmacie.presentation.shared.Result
import esprims.gi2.ma_pharmacie.presentation.shared.UIState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val   sendOtpConfirmationUseCase: SendOtpConfirmationUseCase
) :ViewModel() {

    private val registerStateFlow=MutableStateFlow<UIState<String>>(UIState.Default)
    val _registerStateFlow=registerStateFlow

     fun register(registerDto: RegisterDto):Unit{
         viewModelScope.launch(IO) {
             registerStateFlow.emit(UIState.Loading())

             val result=sendOtpConfirmationUseCase.invoke(registerDto)
             when(result)
        {
            is Result.Success -> registerStateFlow.emit(UIState.Success())

            is Result.Error -> registerStateFlow.emit(UIState.Error(result.message))
        }

         }


    }





}
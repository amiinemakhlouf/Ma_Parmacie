package esprims.gi2.ma_pharmacie.presentation.shared

import androidx.annotation.StringRes

sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    object Default:UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val errorMessage: String) : UIState<Nothing>()

}

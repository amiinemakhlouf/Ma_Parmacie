package esprims.gi2.ma_pharmacie.presentation.shared

import androidx.annotation.StringRes

sealed class UIState<T>(var data:T?=null ) {
    object Default : UIState<String>()

    class Success<T>(data:T?=null) : UIState<T>(data)


    class Loading() : UIState<String>()

    /**
     * Indicates the operation failed with an error message ID.
     *
     * @param errorMessageId The ID to find the string resource.
     */
    class Error( ) : UIState<String>()
}

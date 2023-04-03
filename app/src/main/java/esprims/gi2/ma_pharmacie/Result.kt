package esprims.gi2.ma_pharmacie

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T? =null,val message: String?=null) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

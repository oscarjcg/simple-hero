package com.example.simplehero.utils

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class OpResult<out R> {

    data class Success<out T>(val data: T) : OpResult<T>()
    data class Error(val exception: Exception) : OpResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val OpResult<*>.succeeded
    get() = this is OpResult.Success && data != null

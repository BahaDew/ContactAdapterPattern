package com.example.contactadapterpattern.data

sealed interface ResultData<out T> {
    class Success<T>(val data : T) : ResultData<T>
    class Failure(val message : String) : ResultData<Nothing>

    fun onSuccess(block : (T) -> Unit) : ResultData<T> {
        if(this is Success<T>) block.invoke(data)
        return this
    }

    fun onFailure(block: (message : String) -> Unit) : ResultData<T> {
        if(this is Failure) block.invoke(message)
        return this
    }

    companion object {
        fun <T> success(data : T) = Success(data)
        fun failure(message : String) = Failure(message)
    }
}
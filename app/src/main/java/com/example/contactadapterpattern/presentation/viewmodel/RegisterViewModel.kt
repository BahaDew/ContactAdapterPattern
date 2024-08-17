package com.example.contactadapterpattern.presentation.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RegisterViewModel {

    val progress : StateFlow<Boolean>

    val errorMessage : Flow<String>

    fun onClickNext(
        firstName : String,
        lastName : String,
        phone : String,
        password : String
    )
}
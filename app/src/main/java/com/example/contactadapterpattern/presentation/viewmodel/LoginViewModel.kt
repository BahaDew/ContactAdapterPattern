package com.example.contactadapterpattern.presentation.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val progressGone : StateFlow<Boolean>

    val errorMessage : Flow<String>

    fun onClickSubmit(phone : String, password : String)

    fun onClickRegister()
}
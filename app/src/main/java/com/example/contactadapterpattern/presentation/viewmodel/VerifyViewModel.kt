package com.example.contactadapterpattern.presentation.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface VerifyViewModel {

    val progress: StateFlow<Boolean>

    val errorMessage: Flow<String>

    fun onClickSubmit(
        phone: String,
        code: String
    )
}
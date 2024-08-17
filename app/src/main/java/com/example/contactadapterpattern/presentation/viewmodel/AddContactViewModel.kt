package com.example.contactadapterpattern.presentation.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddContactViewModel {

    val progressState: StateFlow<Boolean>

    val errorMessage: Flow<String>

    fun onClickSave(
        firstName: String,
        lastName: String,
        phone: String
    )

    fun onClickBack()
}
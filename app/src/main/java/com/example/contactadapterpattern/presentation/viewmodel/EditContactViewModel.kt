package com.example.contactadapterpattern.presentation.viewmodel

import com.example.contactadapterpattern.data.model.ContactUIData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface EditContactViewModel {
    val progressState: StateFlow<Boolean>

    val errorMessage: Flow<String>

    fun onClickSave(
        contactUIData: ContactUIData,
        firstName: String,
        lastName: String,
        phone: String,
    )

    fun onClickBack()
}
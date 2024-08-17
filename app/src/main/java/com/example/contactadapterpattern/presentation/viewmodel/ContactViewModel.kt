package com.example.contactadapterpattern.presentation.viewmodel


import com.example.contactadapterpattern.data.model.ContactUIData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ContactViewModel {
    val allContact : SharedFlow<List<ContactUIData>>
    val placeHolderState : StateFlow<Boolean>
    val netWorkConnectionState : StateFlow<Boolean>
    val refreshingState : StateFlow<Boolean>


//    fun requestAllContact()

    fun onClickAdd()

    fun onClickEdit(contact : ContactUIData)

    fun onClickDelete(contact : ContactUIData)

    fun onClickLogOut()
}
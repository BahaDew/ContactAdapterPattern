package com.example.contactadapterpattern.domain

import com.example.contactadapterpattern.data.ResultData
import com.example.contactadapterpattern.data.model.ContactUIData
import com.example.contactadapterpattern.data.source.remote.request.UserLoginRequest
import com.example.contactadapterpattern.data.source.remote.request.UserRegisterRequest
import com.example.contactadapterpattern.data.source.remote.request.UserVerifyRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ContactRepository {

    val allContactList : SharedFlow<List<ContactUIData>>
    val errorMessage : SharedFlow<String>

    fun getAllContact(): Flow<ResultData<List<ContactUIData>>>

    fun addContact(contact: ContactUIData): Flow<ResultData<Unit>>

    fun updateContact(contact: ContactUIData): Flow<ResultData<Unit>>

    fun deleteContact(contact: ContactUIData): Flow<ResultData<Unit>>

    fun loginUser(userLoginRequest: UserLoginRequest): Flow<ResultData<Unit>>

    fun registerUser(userRegisterRequest: UserRegisterRequest): Flow<ResultData<Unit>>

    fun verifyUser(userVerifyRequest: UserVerifyRequest): Flow<ResultData<Unit>>

    fun getLoginState(): Boolean

    fun logOut()
}
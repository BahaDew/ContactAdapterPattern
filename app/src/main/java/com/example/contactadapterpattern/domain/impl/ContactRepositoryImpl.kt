package com.example.contactadapterpattern.domain.impl

import com.example.contactadapterpattern.data.MyPref
import com.example.contactadapterpattern.data.ResultData
import com.example.contactadapterpattern.data.model.ContactUIData
import com.example.contactadapterpattern.data.source.remote.request.CreateContactRequest
import com.example.contactadapterpattern.data.source.remote.request.UpdateContactRequest
import com.example.contactadapterpattern.data.source.remote.request.UserLoginRequest
import com.example.contactadapterpattern.data.source.remote.request.UserRegisterRequest
import com.example.contactadapterpattern.data.source.remote.request.UserVerifyRequest
import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.utils.flowResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val myPref: MyPref,
) : ContactRepository {
    private val fireStore = Firebase.firestore

    override val allContactList = MutableSharedFlow<List<ContactUIData>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    override val errorMessage =
        MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    private val collectionName = "CONTACTS"
    private val cellFirstName = "firstName"
    private val cellLastName = "lastName"
    private val cellPhone = "phone"

    init {
        fireStore.collection(collectionName)
            .addSnapshotListener { value, error ->
                val dataList = ArrayList<ContactUIData>()
                value?.forEach {
                    dataList.add(
                        ContactUIData(
                            id = it.id,
                            firstName = it.data.getOrDefault(cellFirstName, "") as String,
                            lastName = it.data.getOrDefault(cellLastName, "") as String,
                            phone = it.data.getOrDefault(cellPhone, "") as String
                        )
                    )
                }
                allContactList.tryEmit(dataList)
            }
    }

    override fun getAllContact(): Flow<ResultData<List<ContactUIData>>> = flowResponse {

    }

    override fun addContact(
        contact: ContactUIData,
    ): Flow<ResultData<Unit>> = callbackFlow {
        fireStore.collection(collectionName)
            .document(System.currentTimeMillis().toString())
            .set(
                CreateContactRequest(
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    phone = contact.phone
                )
            )
            .addOnCompleteListener {
                trySend(ResultData.success(Unit))
            }
            .addOnFailureListener {
                trySend(ResultData.failure(it.message!!))
            }
        awaitClose()
    }

    override fun updateContact(
        contact: ContactUIData
    ): Flow<ResultData<Unit>> = callbackFlow {
        fireStore.collection(collectionName)
            .document(contact.id)
            .set(
                UpdateContactRequest(
                    firstName = contact.firstName,
                    lastName = contact.lastName,
                    phone = contact.phone
                )
            )
            .addOnCompleteListener {
                trySend(ResultData.success(Unit))
            }
            .addOnFailureListener {
                trySend(ResultData.failure(it.message.toString()))
            }
        awaitClose()
    }

    override fun deleteContact(contact: ContactUIData): Flow<ResultData<Unit>> = callbackFlow {
        fireStore.collection(collectionName)
            .document(contact.id)
            .delete()
            .addOnCompleteListener {
                trySend(ResultData.success(Unit))
            }.addOnFailureListener {
                trySend(ResultData.failure(it.message.toString()))
            }
        awaitClose()
    }

    override fun loginUser(userLoginRequest: UserLoginRequest): Flow<ResultData<Unit>> =
        flowResponse {

        }

    override fun registerUser(userRegisterRequest: UserRegisterRequest): Flow<ResultData<Unit>> =
        flowResponse {

        }

    override fun verifyUser(userVerifyRequest: UserVerifyRequest): Flow<ResultData<Unit>> =
        flowResponse {
            putLoginState(true)
        }

    private val loginState = "KEY_LOGIN_SATE"
    override fun getLoginState(): Boolean {
        return myPref.getBoolean(loginState, false)
    }

    override fun logOut() {
        putLoginState(false)
    }

    private fun putLoginState(state: Boolean) {
        myPref.putBoolean(loginState, state)
    }

    private val tokenKey = "KEY_TOKEN"
    private fun putToken(token: String) {
        myPref.putString(tokenKey, token)
    }

    private fun getToken(): String {
        return myPref.getString(tokenKey, "")
    }
}
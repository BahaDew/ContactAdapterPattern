package com.example.contactadapterpattern.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.model.ContactUIData
import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.navigation.AppNavigator
import com.example.contactadapterpattern.presentation.screens.ContactScreenDirections
import com.example.contactadapterpattern.presentation.viewmodel.ContactViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val navController: AppNavigator
) : ViewModel(), ContactViewModel {
    override val allContact = repository.allContactList
    override val placeHolderState = MutableStateFlow(true)
    override val netWorkConnectionState = MutableStateFlow(true)
    override val refreshingState = MutableStateFlow(true)
    init {
        viewModelScope.launch {
            delay(1000)
            refreshingState.value = false
        }
    }


//    override fun requestAllContact() {
//        refreshingState.value = true
//        repository.getAllContact().onEach {
//            refreshingState.value = false
//            when (it) {
//                is ResultData.Success -> {
//                    allContact.value = it.data
//                    placeHolderState.value = it.data.isNotEmpty()
//                    netWorkConnectionState.value = true
//                }
//
//                is ResultData.Failure -> {
//                    if (it.message == "Nomalum xatolik!" || it.message == "Not Connection Internet")
//                        netWorkConnectionState.value = false
//                    else if (it.message == "Ma'lumot yuborishda xatolik") {
//                        placeHolderState.value = false
//                    }
//                    allContact.value = arrayListOf()
//                }
//            }
//        }.launchIn(viewModelScope)
//    }

    override fun onClickAdd() {
        viewModelScope.launch {
            navController.navigateTo(
                ContactScreenDirections.actionContactScreenToAddContactScreen()
            )
        }
    }

    override fun onClickEdit(contact: ContactUIData) {
        viewModelScope.launch {
            navController.navigateTo(
                ContactScreenDirections.actionContactScreenToEditContactScreen(
                    contact
                )
            )
        }
    }

    override fun onClickDelete(contact: ContactUIData) {
        refreshingState.value = true
        repository.deleteContact(contact = contact)
            .onEach {
                refreshingState.value = false
                it.onSuccess {
//                    requestAllContact()
                }
                it.onFailure {

                }
            }.launchIn(viewModelScope)
    }

    override fun onClickLogOut() {
        repository.logOut()
        viewModelScope.launch {
            navController.navigateTo(ContactScreenDirections.actionContactScreenToLoginScreen())
        }
    }
}
package com.example.contactadapterpattern.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import com.example.contactadapterpattern.data.source.remote.request.UserLoginRequest
import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.navigation.AppNavigator
import com.example.contactadapterpattern.presentation.screens.LoginScreenDirections
import com.example.contactadapterpattern.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val appNavigator: AppNavigator
) : ViewModel(), LoginViewModel {

    override val progressGone = MutableStateFlow(true)

    private var _errorMessage: ((String) -> Unit)? = null
    override val errorMessage: Flow<String> = channelFlow {
        _errorMessage = { trySend(it) }
        awaitClose { _errorMessage = null }
    }

    override fun onClickSubmit(phone: String, password: String) {
        progressGone.value = false
        repository.loginUser(
            UserLoginRequest(phone = phone, password = password)
        ).onEach {
            when (it) {
                is ResultData.Success -> {
                    progressGone.value = true
                    appNavigator.navigateTo(LoginScreenDirections.actionLoginScreenToContactScreen())
                }

                is ResultData.Failure -> {
                    _errorMessage?.invoke(it.message)
                    progressGone.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onClickRegister() {
        viewModelScope.launch {
            appNavigator.navigateTo(LoginScreenDirections.actionLoginScreenToRegisterScreen())
        }
    }
}
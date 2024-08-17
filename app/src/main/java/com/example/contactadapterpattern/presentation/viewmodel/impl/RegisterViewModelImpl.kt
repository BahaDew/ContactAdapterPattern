package com.example.contactadapterpattern.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import com.example.contactadapterpattern.data.source.remote.request.UserRegisterRequest
import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.navigation.AppNavigator
import com.example.contactadapterpattern.presentation.screens.RegisterScreenDirections
import com.example.contactadapterpattern.presentation.viewmodel.RegisterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val navController: AppNavigator
) :
    ViewModel(),
    RegisterViewModel {
    override val progress = MutableStateFlow(true)
    private var _errorMessage: ((String) -> Unit)? = null
    override val errorMessage = channelFlow {
        _errorMessage = { trySend(it) }
        awaitClose { _errorMessage = null }
    }

    override fun onClickNext(firstName: String, lastName: String, phone: String, password: String) {
        progress.value = false
        repository.registerUser(
            UserRegisterRequest(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                password = password
            )
        ).onEach {
            progress.value = true
            when (it) {
                is ResultData.Success -> {
                    navController.navigateTo(
                        RegisterScreenDirections.actionRegisterScreenToVerifyCodeScreen(
                            phone = phone
                        )
                    )
                }

                is ResultData.Failure -> {
                    _errorMessage?.invoke(it.message)
                }
            }
        }.launchIn(viewModelScope)
    }
}
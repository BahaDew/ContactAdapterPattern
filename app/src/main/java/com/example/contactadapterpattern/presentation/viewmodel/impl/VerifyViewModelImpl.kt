package com.example.contactadapterpattern.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import com.example.contactadapterpattern.data.source.remote.request.UserVerifyRequest
import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.navigation.AppNavigator
import com.example.contactadapterpattern.presentation.screens.VerifyCodeScreenDirections
import com.example.contactadapterpattern.presentation.viewmodel.VerifyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val navController: AppNavigator
) : ViewModel(), VerifyViewModel {

    private var _errorMessage: ((String) -> Unit)? = null
    override val errorMessage = channelFlow {
        _errorMessage = { trySend(it) }
        awaitClose { _errorMessage = null }
    }
    override val progress = MutableStateFlow(true)

    override fun onClickSubmit(phone: String, code: String) {
        progress.value = false
        repository.verifyUser(UserVerifyRequest(phone = phone, code = code))
            .onEach {
                progress.value = true
                when (it) {
                    is ResultData.Success -> {
                        navController.navigateTo(VerifyCodeScreenDirections.actionVerifyCodeScreenToContactScreen())
                    }

                    is ResultData.Failure -> {
                        _errorMessage?.invoke(it.message)
                    }
                }
            }.launchIn(viewModelScope)
    }
}
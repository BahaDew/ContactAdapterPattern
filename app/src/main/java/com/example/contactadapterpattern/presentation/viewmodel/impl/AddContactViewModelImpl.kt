package com.example.contactadapterpattern.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import com.example.contactadapterpattern.data.model.ContactUIData
import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.navigation.AppNavigator
import com.example.contactadapterpattern.presentation.viewmodel.AddContactViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddContactViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val navigator: AppNavigator
) : ViewModel(), AddContactViewModel {

    private var _errorMessage: ((String) -> Unit)? = null

    override val errorMessage = channelFlow {
        _errorMessage = { trySend(it) }
        awaitClose { _errorMessage = null }
    }
    override val progressState = MutableStateFlow(true)

    override fun onClickSave(firstName: String, lastName: String, phone: String) {
        progressState.value = false
        repository.addContact(
            ContactUIData(
                id = "",
                firstName = firstName,
                lastName = lastName,
                phone = phone,
            )
        ).onEach {
            progressState.value = true
            when (it) {
                is ResultData.Success -> {
                    navigator.navigateUp()
                }

                is ResultData.Failure -> {
                    _errorMessage?.invoke(it.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onClickBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }
}
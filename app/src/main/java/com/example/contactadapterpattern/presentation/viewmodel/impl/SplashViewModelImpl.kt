package com.example.contactadapterpattern.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.navigation.AppNavigator
import com.example.contactadapterpattern.presentation.screens.SplashScreenDirections
import com.example.contactadapterpattern.presentation.viewmodel.SplashViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val appNavigator: AppNavigator
) : ViewModel(), SplashViewModel {
    override val openSplash = MutableStateFlow(Unit)

    init {
        viewModelScope.launch {
            delay(1500)
            openSplash.value = Unit
            val direction = SplashScreenDirections.actionSplashScreenToContactScreen()
            appNavigator.navigateTo(direction)
        }
    }
}
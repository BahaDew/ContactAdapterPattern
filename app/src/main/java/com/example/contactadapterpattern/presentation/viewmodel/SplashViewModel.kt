package com.example.contactadapterpattern.presentation.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface SplashViewModel {
    val openSplash : StateFlow<Unit>
}
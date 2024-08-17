package com.example.contactadapterpattern.navigation

import androidx.navigation.NavDirections

interface AppNavigator {

    suspend fun navigateTo(direction: NavDirections)

    suspend fun navigateUp()
}
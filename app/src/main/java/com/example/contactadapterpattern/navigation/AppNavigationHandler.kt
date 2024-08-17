package com.example.contactadapterpattern.navigation

import kotlinx.coroutines.flow.Flow

interface AppNavigationHandler {
    val buffer : Flow<AppNavigation>
}
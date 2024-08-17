package com.example.contactadapterpattern.di

import androidx.navigation.NavController
import com.example.contactadapterpattern.navigation.AppNavigationDispatcher
import com.example.contactadapterpattern.navigation.AppNavigationHandler
import com.example.contactadapterpattern.navigation.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindAppNavigation(impl : AppNavigationDispatcher) : AppNavigator

    @Binds
    fun bindAppNavHandler(impl : AppNavigationDispatcher) : AppNavigationHandler
}
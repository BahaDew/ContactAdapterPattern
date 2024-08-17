package com.example.contactadapterpattern.di

import com.example.contactadapterpattern.domain.ContactRepository
import com.example.contactadapterpattern.domain.impl.ContactRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideContactRepository(impl: ContactRepositoryImpl): ContactRepository
}
package com.example.mydearmovies.di

import com.example.mydearmovies.core.error.AndroidErrorMessageResProvider
import com.example.mydearmovies.core.error.ErrorMessageResProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorMessageModule {

    @Binds
    @Singleton
    abstract fun bindErrorMessageResProvider(impl: AndroidErrorMessageResProvider): ErrorMessageResProvider
}

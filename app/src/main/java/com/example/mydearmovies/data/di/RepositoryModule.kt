package com.example.mydearmovies.data.di

import com.example.mydearmovies.data.repository.HomeRepositoryImpl
import com.example.mydearmovies.data.repository.MediaRepositoryImpl
import com.example.mydearmovies.data.repository.PeopleRepositoryImpl
import com.example.mydearmovies.domain.repository.HomeRepository
import com.example.mydearmovies.domain.repository.MediaRepository
import com.example.mydearmovies.domain.repository.PeopleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindPeopleRepository(impl: PeopleRepositoryImpl): PeopleRepository
}

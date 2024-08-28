package com.chicohan.samplelistapp.di

import com.chicohan.samplelistapp.data.repository.AuthenticationRepository
import com.chicohan.samplelistapp.data.repository.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

//    @Binds
//    @ViewModelScoped
//    abstract fun bindAuthenticationRepository(repo: AuthenticationRepositoryImpl): AuthenticationRepository
}
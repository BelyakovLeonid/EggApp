package com.example.eggyapp.di.modules

import com.example.eggyapp.data.SetupEggRepository
import com.example.eggyapp.data.SetupEggRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SetupModule {
    @Singleton
    @Provides
    fun provideSetupRepository(): SetupEggRepository = SetupEggRepositoryImpl()
}
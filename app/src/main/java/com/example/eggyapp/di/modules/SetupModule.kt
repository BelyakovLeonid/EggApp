package com.example.eggyapp.di.modules

import com.example.eggyapp.data.SetupEggRepository
import com.example.eggyapp.data.SetupEggRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface SetupModule {
    @Binds
    @Singleton
    fun bindsSetupRepository(impl: SetupEggRepositoryImpl): SetupEggRepository
}
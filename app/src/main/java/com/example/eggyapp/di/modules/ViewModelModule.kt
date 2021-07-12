package com.example.eggyapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggyapp.di.ViewModelFactory
import com.example.eggyapp.di.ViewModelKey
import com.example.eggyapp.ui.cook.CookViewModel
import com.example.eggyapp.ui.setup.SetupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CookViewModel::class)
    fun bindsCookViewModel(viewModel: CookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel::class)
    fun bindsSetupViewModel(viewModel: SetupViewModel): ViewModel

    @Binds
    fun bindsViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory
}
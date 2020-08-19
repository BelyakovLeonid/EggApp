package com.example.eggyapp.di.modules

import androidx.lifecycle.ViewModel
import com.example.eggyapp.di.ViewModelKey
import com.example.eggyapp.ui.cook.CookViewModel
import com.example.eggyapp.ui.setup.SetupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CookViewModel::class)
    abstract fun cookViewModel(viewModel: CookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel::class)
    abstract fun setupViewModel(viewModel: SetupViewModel): ViewModel
}
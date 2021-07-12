package com.example.eggyapp.di

import com.example.eggyapp.di.modules.SetupModule
import com.example.eggyapp.di.modules.ViewModelModule
import com.example.eggyapp.ui.cook.CookFragment
import com.example.eggyapp.ui.setup.SetupFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        SetupModule::class
    ]
)
interface AppComponent {
    fun inject(fragment: SetupFragment)
    fun inject(fragment: CookFragment)

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }
}
package leo.apps.eggy.base.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import leo.apps.eggy.base.di.ViewModelFactory
import leo.apps.eggy.base.di.ViewModelKey
import leo.apps.eggy.cook.presentation.CookViewModel
import leo.apps.eggy.setup.presentation.SetupViewModel

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CookViewModel::class)
    fun bindsCookViewModel(impl: CookViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel::class)
    fun bindsSetupViewModel(impl: SetupViewModel): ViewModel

    @Binds
    fun bindsViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory
}
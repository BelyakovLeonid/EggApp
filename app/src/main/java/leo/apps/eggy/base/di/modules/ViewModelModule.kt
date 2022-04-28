package leo.apps.eggy.base.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import leo.apps.eggy.base.di.ViewModelFactory
import leo.apps.eggy.base.di.ViewModelKey
import leo.apps.eggy.cook.presentation.CookViewModel
import leo.apps.eggy.root.presentation.MainViewModel
import leo.apps.eggy.setup.presentation.SetupViewModel
import leo.apps.eggy.welcome.presentation.WelcomeViewModel

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
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    fun bindsWelcomeViewModel(impl: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindsMainViewModel(impl: MainViewModel): ViewModel

    @Binds
    fun bindsViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory
}
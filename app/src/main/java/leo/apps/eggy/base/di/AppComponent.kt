package leo.apps.eggy.base.di

import dagger.Component
import leo.apps.eggy.base.di.modules.SetupModule
import leo.apps.eggy.base.di.modules.ViewModelModule
import leo.apps.eggy.cook.presentation.CookFragment
import leo.apps.eggy.setup.presentation.SetupFragment
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
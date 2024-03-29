package leo.apps.eggy.base.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import leo.apps.eggy.base.di.modules.BaseModule
import leo.apps.eggy.base.di.modules.SetupModule
import leo.apps.eggy.base.di.modules.ViewModelModule
import leo.apps.eggy.cook.presentation.CookFragment
import leo.apps.eggy.root.presentation.MainActivity
import leo.apps.eggy.setup.presentation.SetupFragment
import leo.apps.eggy.welcome.presentation.WelcomeFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        BaseModule::class,
        ViewModelModule::class,
        SetupModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: SetupFragment)
    fun inject(fragment: CookFragment)
    fun inject(fragment: WelcomeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}
package leo.apps.eggy.base.di.modules

import dagger.Binds
import dagger.Module
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.SetupEggRepositoryImpl
import javax.inject.Singleton

@Module
interface SetupModule {
    @Binds
    @Singleton
    fun bindsSetupRepository(impl: SetupEggRepositoryImpl): SetupEggRepository
}
package leo.apps.eggy.base.di.modules

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import leo.apps.eggy.base.analytics.data.FirebaseAnalyticsRepository
import leo.apps.eggy.base.analytics.domain.AnalyticsRepository

@Module(includes = [BaseBindsModule::class])
object BaseModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideFirebaseAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)
}

@Module
interface BaseBindsModule {

    @Binds
    fun bindsFirebaseAnalyticsRepository(impl: FirebaseAnalyticsRepository): AnalyticsRepository
}
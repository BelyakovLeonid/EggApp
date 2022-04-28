package leo.apps.eggy.base.analytics.domain

import androidx.fragment.app.Fragment
import leo.apps.eggy.base.analytics.Analytics
import javax.inject.Inject

class AnalyticsInteractor @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) {

    fun trackScreen(fragmentClass: Class<Fragment>, screenName: String) {
        analyticsRepository.trackScreen(screenName, fragmentClass.simpleName)
    }

    fun trackEvent(name: String, action: String) {
        val eventParams = mapOf(Analytics.Constants.PARAM_ACTION to action)
        analyticsRepository.trackEvent(name, eventParams)
    }
}
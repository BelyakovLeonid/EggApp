package leo.apps.eggy.base.analytics.data

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import leo.apps.eggy.base.analytics.domain.AnalyticsRepository
import leo.apps.eggy.base.utils.toBundle
import javax.inject.Inject

class FirebaseAnalyticsRepository @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsRepository {

    override fun trackEvent(name: String, parameters: Map<String, String>) {
        firebaseAnalytics.logEvent(name, parameters.toBundle())
    }

    override fun trackScreen(screenName: String, screenClass: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }
}
package leo.apps.eggy.base.analytics.domain

interface AnalyticsRepository {
    fun trackEvent(name: String, parameters: Map<String, String>)
    fun trackScreen(screenName: String, screenClass: String)
}
package leo.apps.eggy.welcome.presentation

import leo.apps.eggy.base.analytics.Analytics
import leo.apps.eggy.base.analytics.domain.AnalyticsInteractor
import leo.apps.eggy.base.presentation.BaseViewModel
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val analyticsInteractor: AnalyticsInteractor
) : BaseViewModel(analyticsInteractor) {

    fun onButtonClick() {
        analyticsInteractor.trackEvent(
            Analytics.Welcome.EVENT_NAME,
            Analytics.Welcome.COOK_ACTION
        )
    }
}
package leo.apps.eggy.root.presentation

import androidx.lifecycle.ViewModel
import leo.apps.eggy.base.analytics.Analytics
import leo.apps.eggy.base.analytics.domain.AnalyticsInteractor
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val analyticsInteractor: AnalyticsInteractor
) : ViewModel() {

    fun onBackPressed() {
        analyticsInteractor.trackEvent(
            Analytics.Main.EVENT_NAME,
            Analytics.Main.BACK_ACTION
        )
    }

    fun onBackPressedConfirmed() {
        analyticsInteractor.trackEvent(
            Analytics.Main.EVENT_NAME,
            Analytics.Main.BACK_CONFIRM_ACTION
        )
    }
}
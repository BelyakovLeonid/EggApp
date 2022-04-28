package leo.apps.eggy.base.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import leo.apps.eggy.base.analytics.domain.AnalyticsInteractor

abstract class BaseViewModel(
    private val analyticsInteractor: AnalyticsInteractor
) : ViewModel() {

    fun trackScreen(screenClass: Class<Fragment>, screenName: String) {
        analyticsInteractor.trackScreen(screenClass, screenName)
    }
}
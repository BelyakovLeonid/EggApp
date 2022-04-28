package leo.apps.eggy.setup.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.plus
import leo.apps.eggy.base.analytics.Analytics
import leo.apps.eggy.base.analytics.domain.AnalyticsInteractor
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.model.SetupSize
import leo.apps.eggy.base.data.model.SetupTemperature
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.presentation.BaseViewModel
import leo.apps.eggy.base.utils.getByIndex
import leo.apps.eggy.base.utils.getIndexOf
import leo.apps.eggy.setup.presentation.model.SetupUiState
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository,
    private val analyticsInteractor: AnalyticsInteractor
) : BaseViewModel(analyticsInteractor) {

    private val mutableState = MutableStateFlow(SetupUiState.DEFAULT)
    val state = mutableState.asStateFlow()

    init {
        observeData()
    }

    fun onSelectTemperatureIndex(index: Int) {
        val temperature = getByIndex<SetupTemperature>(index)
        setupRepository.setTemperature(temperature)
        analyticsInteractor.trackEvent(
            Analytics.Setup.EVENT_NAME,
            "${Analytics.Setup.TEMPERATURE_SELECT_ACTION}$index"
        )
    }

    fun onSelectSizeIndex(index: Int) {
        val size = getByIndex<SetupSize>(index)
        setupRepository.setSize(size)
        analyticsInteractor.trackEvent(
            Analytics.Setup.EVENT_NAME,
            "${Analytics.Setup.SIZE_SELECT_ACTION}$index"
        )
    }

    fun onSelectTypeIndex(index: Int) {
        val type = getByIndex<SetupType>(index)
        setupRepository.setType(type)
        analyticsInteractor.trackEvent(
            Analytics.Setup.EVENT_NAME,
            "${Analytics.Setup.TYPE_SELECT_ACTION}$index"
        )
    }

    fun onStartClick() {
        analyticsInteractor.trackEvent(
            Analytics.Setup.EVENT_NAME,
            Analytics.Setup.NEXT_ACTION
        )
    }

    private fun observeData() {
        combine(
            setupRepository.calculatedTimeFlow,
            setupRepository.selectedTypeFlow,
            setupRepository.selectedTemperatureFlow,
            setupRepository.selectedSizeFlow
        ) { time, type, temperature, size ->
            mutableState.value = state.value.copy(
                calculatedTime = time,
                isButtonNextEnable = time != 0,
                selectedTypeIndex = getIndexOf(type),
                selectedTemperatureIndex = getIndexOf(temperature),
                selectedSizeIndex = getIndexOf(size)
            )
        }.launchIn(viewModelScope + Dispatchers.Default)
    }
}
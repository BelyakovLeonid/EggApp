package leo.apps.eggy.setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.plus
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.model.SetupSize
import leo.apps.eggy.base.data.model.SetupTemperature
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.utils.getByIndex
import leo.apps.eggy.base.utils.getIndexOf
import leo.apps.eggy.setup.presentation.model.SetupUiState
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(SetupUiState.DEFAULT)
    val state = mutableState.asStateFlow()

    init {
        observeData()
    }

    fun onSelectTemperatureIndex(index: Int) {
        val temperature = getByIndex<SetupTemperature>(index)
        setupRepository.setTemperature(temperature)
    }

    fun onSelectSizeIndex(index: Int) {
        val size = getByIndex<SetupSize>(index)
        setupRepository.setSize(size)
    }

    fun onSelectTypeIndex(index: Int) {
        val type = getByIndex<SetupType>(index)
        setupRepository.setType(type)
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
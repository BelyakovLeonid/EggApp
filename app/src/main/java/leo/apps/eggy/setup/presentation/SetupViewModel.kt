package leo.apps.eggy.setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    val state = MutableStateFlow(SetupUiState.DEFAULT)

    init {
        observeSetupData()
    }

    private fun observeSetupData() {
        combine(
            setupRepository.calculatedTimeFlow,
            setupRepository.selectedTypeFlow,
            setupRepository.selectedSizeFlow,
            setupRepository.selectedTemperatureFlow
        ) { time, type, size, temperature ->
            SetupUiState(
                calculatedTime = time,
                isButtonNextEnable = time != 0,
                selectedTypeIndex = getIndexOf(type),
                selectedSizeIndex = getIndexOf(size),
                selectedTemperatureIndex = getIndexOf(temperature)
            )
        }.onEach { model ->
            state.value = model
        }.launchIn(viewModelScope + Dispatchers.Default)
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
}
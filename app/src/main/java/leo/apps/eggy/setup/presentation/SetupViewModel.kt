package leo.apps.eggy.setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.model.SetupSize
import leo.apps.eggy.base.data.model.SetupTemperature
import leo.apps.eggy.base.data.model.SetupType
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {

    val calculatedTime = MutableStateFlow(0)
    val selectedTemperature = MutableStateFlow(SetupTemperature.NONE)
    val selectedSize = MutableStateFlow(SetupSize.NONE)
    val selectedType = MutableStateFlow(SetupType.NONE)
    val isCookEnable = MutableStateFlow(false)

    init {
        observeCalculatedTime()
        observeSelectedTemperature()
        observeSelectedSize()
        observeSelectedType()
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeFlow
            .onEach {
                calculatedTime.value = it
                isCookEnable.value = it != 0
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeFlow
            .onEach {
                selectedType.value = it
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedSize() {
        setupRepository.selectedSizeFlow
            .onEach {
                selectedSize.value = it
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedTemperature() {
        setupRepository.selectedTemperatureFlow
            .onEach {
                selectedTemperature.value = it
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    fun onSelectTemperature(temperature: SetupTemperature?) {
        setupRepository.setTemperature(temperature)
    }

    fun onSelectSize(size: SetupSize?) {
        setupRepository.setSize(size)
    }

    fun onSelectType(type: SetupType?) {
        setupRepository.setType(type)
    }
}
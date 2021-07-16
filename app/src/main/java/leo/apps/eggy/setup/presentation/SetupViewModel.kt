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
import leo.apps.eggy.base.utils.getByIndex
import leo.apps.eggy.base.utils.getIndexOf
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {

    val calculatedTime = MutableStateFlow(0)
    val selectedTemperature = MutableStateFlow(-1)
    val selectedSize = MutableStateFlow(-1)
    val selectedType = MutableStateFlow(-1)
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
                selectedType.value = getIndexOf(it)
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedSize() {
        setupRepository.selectedSizeFlow
            .onEach {
                selectedSize.value = getIndexOf(it)
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedTemperature() {
        setupRepository.selectedTemperatureFlow
            .onEach {
                selectedTemperature.value = getIndexOf(it)
            }.launchIn(viewModelScope + Dispatchers.IO)
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
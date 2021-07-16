package leo.apps.eggy.cook.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.model.SetupType
import javax.inject.Inject

class CookViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {
    val calculatedTime = MutableStateFlow(0)
    val selectedType = MutableStateFlow<SetupType?>(null)

    init {
        observeCalculatedTime()
        observeSelectedType()
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeFlow
            .onEach { calculatedTime.value = it }
            .launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeFlow
            .onEach { selectedType.value = it }
            .launchIn(viewModelScope + Dispatchers.IO)
    }
}
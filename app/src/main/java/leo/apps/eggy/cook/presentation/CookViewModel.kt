package leo.apps.eggy.cook.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import leo.apps.eggy.R
import leo.apps.eggy.base.data.SetupEggRepository
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.utils.toTimerString
import leo.apps.eggy.cook.presentation.model.CookUiState
import javax.inject.Inject

class CookViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {

    val state = MutableStateFlow(CookUiState.DEFAULT)

    init {
        observeCalculatedTime()
        observeSelectedType()
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeFlow
            .onEach { time ->
                state.value = state.value.copy(
                    calculatedTime = time,
                    timerText = time.toTimerString()
                )
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeFlow
            .onEach { type ->
                state.value = state.value.copy(
                    selectedType = type,
                    titleTextId = getTitleByType(type)
                )
            }.launchIn(viewModelScope + Dispatchers.IO)
    }

    @StringRes
    private fun getTitleByType(type: SetupType?): Int = when (type) {
        SetupType.SOFT -> R.string.cook_eggs_soft
        SetupType.MEDIUM -> R.string.cook_eggs_medium
        else -> R.string.cook_eggs_hard
    }
}
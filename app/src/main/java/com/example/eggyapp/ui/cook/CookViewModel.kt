package com.example.eggyapp.ui.cook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eggyapp.data.SetupEggRepository
import com.example.eggyapp.data.model.SetupType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import javax.inject.Inject

class CookViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {
    val calculatedTime = MutableStateFlow(0)
    val selectedType = MutableStateFlow(SetupType.NONE)

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
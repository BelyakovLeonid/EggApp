package com.example.eggyapp.data

import com.example.eggyapp.data.model.SetupSize
import com.example.eggyapp.data.model.SetupTemperature
import com.example.eggyapp.data.model.SetupType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface SetupEggRepository {
    fun setTemperature(temperature: SetupTemperature?)
    fun setSize(size: SetupSize?)
    fun setType(type: SetupType?)

    val calculatedTimeFlow: Flow<Int>
    val selectedTemperatureFlow: Flow<SetupTemperature>
    val selectedSizeFlow: Flow<SetupSize>
    val selectedTypeFlow: Flow<SetupType>
}

class SetupEggRepositoryImpl @Inject constructor() : SetupEggRepository {

    override var calculatedTimeFlow= MutableStateFlow(0)
    override var selectedTemperatureFlow= MutableStateFlow(SetupTemperature.NONE)
    override var selectedSizeFlow= MutableStateFlow(SetupSize.NONE)
    override var selectedTypeFlow= MutableStateFlow(SetupType.NONE)

    private val timeMap: HashMap<Triple<SetupTemperature, SetupSize, SetupType>, Int> = hashMapOf(
        Triple(SetupTemperature.FRIDGE, SetupSize.S, SetupType.SOFT) to 10_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.S, SetupType.MEDIUM) to 350_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.S, SetupType.HARD) to 480_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.M, SetupType.SOFT) to 290_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.M, SetupType.MEDIUM) to 390_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.M, SetupType.HARD) to 530_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.L, SetupType.SOFT) to 320_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.L, SetupType.MEDIUM) to 440_000,
        Triple(SetupTemperature.FRIDGE, SetupSize.L, SetupType.HARD) to 580_000,

        Triple(SetupTemperature.ROOM, SetupSize.S, SetupType.SOFT) to 160_000,
        Triple(SetupTemperature.ROOM, SetupSize.S, SetupType.MEDIUM) to 260_000,
        Triple(SetupTemperature.ROOM, SetupSize.S, SetupType.HARD) to 380_000,
        Triple(SetupTemperature.ROOM, SetupSize.M, SetupType.SOFT) to 190_000,
        Triple(SetupTemperature.ROOM, SetupSize.M, SetupType.MEDIUM) to 290_000,
        Triple(SetupTemperature.ROOM, SetupSize.M, SetupType.HARD) to 430_000,
        Triple(SetupTemperature.ROOM, SetupSize.L, SetupType.SOFT) to 210_000,
        Triple(SetupTemperature.ROOM, SetupSize.L, SetupType.MEDIUM) to 320_000,
        Triple(SetupTemperature.ROOM, SetupSize.L, SetupType.HARD) to 480_000
    )

    override fun setTemperature(temperature: SetupTemperature?) {
        selectedTemperatureFlow.value = temperature ?: SetupTemperature.NONE
        recalculateTime()
    }

    override fun setSize(size: SetupSize?) {
        selectedSizeFlow.value = size ?: SetupSize.NONE
        recalculateTime()
    }

    override fun setType(type: SetupType?) {
        selectedTypeFlow.value = type ?: SetupType.NONE
        recalculateTime()
    }

    private fun recalculateTime() {
        calculatedTimeFlow.value =
            timeMap[Triple(
                selectedTemperatureFlow.value,
                selectedSizeFlow.value,
                selectedTypeFlow.value
            )] ?: 0
    }
}
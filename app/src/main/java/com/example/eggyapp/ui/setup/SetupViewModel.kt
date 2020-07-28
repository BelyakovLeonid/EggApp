package com.example.eggyapp.ui.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eggyapp.ui.setup.SetupSize.*
import com.example.eggyapp.ui.setup.SetupTemperature.FRIDGE_TEMPERATURE
import com.example.eggyapp.ui.setup.SetupTemperature.ROOM_TEMPERATURE
import com.example.eggyapp.ui.setup.SetupType.*

class SetupViewModel : ViewModel() {

    private val eventOpenCookScreen = MutableLiveData<Unit>() //todo use liveEvent
    val openCookScreen: LiveData<Unit> = eventOpenCookScreen

    private val mutableCalculatedTime = MutableLiveData<Int?>()
    val calculatedTime: LiveData<Int?> = mutableCalculatedTime

    private var temperature: SetupTemperature? = null
    private var size: SetupSize? = null
    private var type: SetupType? = null

    fun onStartClicked() {
        if (temperature != null && size != null && type != null) {
            eventOpenCookScreen.postValue(Unit)
        }
    }

    fun onSelectTemperature(temperature: SetupTemperature?) {
        this.temperature = temperature
        recalculateTime()
    }

    fun onSelectSize(size: SetupSize?) {
        this.size = size
        recalculateTime()
    }

    fun onSelectType(type: SetupType?) {
        this.type = type
        recalculateTime()
    }

    private val map: HashMap<Triple<SetupTemperature, SetupSize, SetupType>, Int> = hashMapOf(
        Triple(FRIDGE_TEMPERATURE, SIZE_S, SOFT_TYPE) to 260_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_S, MEDIUM_TYPE) to 350_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_S, HARD_TYPE) to 480_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_M, SOFT_TYPE) to 290_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_M, MEDIUM_TYPE) to 390_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_M, HARD_TYPE) to 530_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_L, SOFT_TYPE) to 320_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_L, MEDIUM_TYPE) to 440_000,
        Triple(FRIDGE_TEMPERATURE, SIZE_L, HARD_TYPE) to 580_000,

        Triple(ROOM_TEMPERATURE, SIZE_S, SOFT_TYPE) to 160_000,
        Triple(ROOM_TEMPERATURE, SIZE_S, MEDIUM_TYPE) to 260_000,
        Triple(ROOM_TEMPERATURE, SIZE_S, HARD_TYPE) to 380_000,
        Triple(ROOM_TEMPERATURE, SIZE_M, SOFT_TYPE) to 190_000,
        Triple(ROOM_TEMPERATURE, SIZE_M, MEDIUM_TYPE) to 290_000,
        Triple(ROOM_TEMPERATURE, SIZE_M, HARD_TYPE) to 430_000,
        Triple(ROOM_TEMPERATURE, SIZE_L, SOFT_TYPE) to 210_000,
        Triple(ROOM_TEMPERATURE, SIZE_L, MEDIUM_TYPE) to 320_000,
        Triple(ROOM_TEMPERATURE, SIZE_L, HARD_TYPE) to 480_000
    )

    private fun recalculateTime() {
        mutableCalculatedTime.value = map[Triple(temperature, size, type)]
    }
}
package com.example.eggyapp.data

import com.example.eggyapp.ui.setup.SetupSize
import com.example.eggyapp.ui.setup.SetupSize.*
import com.example.eggyapp.ui.setup.SetupTemperature
import com.example.eggyapp.ui.setup.SetupTemperature.FRIDGE_TEMPERATURE
import com.example.eggyapp.ui.setup.SetupTemperature.ROOM_TEMPERATURE
import com.example.eggyapp.ui.setup.SetupType
import com.example.eggyapp.ui.setup.SetupType.*
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.runBlocking

interface SetupEggRepository {
    fun postTemperature(temperature: SetupTemperature?)
    fun postSize(size: SetupSize?)
    fun postType(type: SetupType?)
    fun isTimeCalculated(): Boolean

    val calculatedTimeStream: Observable<Int>
}

class SetupEggRepositoryImpl : SetupEggRepository {

    private var calculatedTimeSubject: BehaviorSubject<Int> = BehaviorSubject.create()
    private var temperature: SetupTemperature? = null
    private var size: SetupSize? = null
    private var type: SetupType? = null

    private val timeMap: HashMap<Triple<SetupTemperature, SetupSize, SetupType>, Int> = hashMapOf(
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


    override fun postTemperature(temperature: SetupTemperature?) {
        this.temperature = temperature
        recalculateTime()
    }

    override fun postSize(size: SetupSize?) {
        this.size = size
        recalculateTime()
    }

    override fun postType(type: SetupType?) {
        this.type = type
        recalculateTime()
    }

    private fun recalculateTime() = runBlocking {
        calculatedTimeSubject.onNext(timeMap[Triple(temperature, size, type)] ?: 0)
    }

    override fun isTimeCalculated() = temperature != null && size != null && type != null

    override val calculatedTimeStream: Observable<Int>
        get() = calculatedTimeSubject
}
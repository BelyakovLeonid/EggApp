package com.example.eggyapp.data

import com.example.eggyapp.data.SetupSize.*
import com.example.eggyapp.data.SetupTemperature.FRIDGE_TEMPERATURE
import com.example.eggyapp.data.SetupTemperature.ROOM_TEMPERATURE
import com.example.eggyapp.data.SetupType.*
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface SetupEggRepository {
    fun postTemperature(temperature: SetupTemperature?)
    fun postSize(size: SetupSize?)
    fun postType(type: SetupType?)

    val calculatedTimeStream: Observable<Int>
    val selectedTypeStream: Observable<SetupType>
}

class SetupEggRepositoryImpl : SetupEggRepository {

    private var selectedTypeSubject: BehaviorSubject<SetupType> = BehaviorSubject.create()
    private var calculatedTimeSubject: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)

    private var temperature: SetupTemperature? = null
    private var size: SetupSize? = null
    private var type: SetupType? = null

    private val timeMap: HashMap<Triple<SetupTemperature, SetupSize, SetupType>, Int> = hashMapOf(
        Triple(FRIDGE_TEMPERATURE, SIZE_S, SOFT_TYPE) to 10_000,
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
        if (type != null) selectedTypeSubject.onNext(type)
        recalculateTime()
    }

    private fun recalculateTime() {
        calculatedTimeSubject.onNext(timeMap[Triple(temperature, size, type)] ?: 0)
    }

    override val calculatedTimeStream: Observable<Int>
        get() = calculatedTimeSubject

    override val selectedTypeStream: Observable<SetupType>
        get() = selectedTypeSubject
}

interface Identifiable {
    val id: Int
}

enum class SetupTemperature(override val id: Int) : Identifiable {
    FRIDGE_TEMPERATURE(0), ROOM_TEMPERATURE(1)
}

enum class SetupSize(override val id: Int) : Identifiable {
    SIZE_S(0), SIZE_M(1), SIZE_L(2)
}

enum class SetupType(override val id: Int) : Identifiable {
    SOFT_TYPE(0), MEDIUM_TYPE(1), HARD_TYPE(2)
}
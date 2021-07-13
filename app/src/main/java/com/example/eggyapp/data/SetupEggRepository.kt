package com.example.eggyapp.data

import com.example.eggyapp.data.model.SetupSize
import com.example.eggyapp.data.model.SetupTemperature
import com.example.eggyapp.data.model.SetupType
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface SetupEggRepository {
    fun setTemperature(temperature: SetupTemperature?)
    fun setSize(size: SetupSize?)
    fun setType(type: SetupType?)

    val calculatedTimeStream: Observable<Int>
    val selectedTemperatureStream: Observable<SetupTemperature>
    val selectedSizeStream: Observable<SetupSize>
    val selectedTypeStream: Observable<SetupType>
}

class SetupEggRepositoryImpl @Inject constructor() : SetupEggRepository {

    private var selectedSizeSubject: BehaviorSubject<SetupSize> = BehaviorSubject.create()
    private var selectedTypeSubject: BehaviorSubject<SetupType> = BehaviorSubject.create()
    private var selectedTempSubject: BehaviorSubject<SetupTemperature> = BehaviorSubject.create()

    private var calculatedTimeSubject: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)

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
        selectedTempSubject.onNext(temperature ?: SetupTemperature.NONE)
        recalculateTime()
    }

    override fun setSize(size: SetupSize?) {
        selectedSizeSubject.onNext(size ?: SetupSize.NONE)
        recalculateTime()
    }

    override fun setType(type: SetupType?) {
        selectedTypeSubject.onNext(type ?: SetupType.NONE)
        recalculateTime()
    }

    private fun recalculateTime() {
        calculatedTimeSubject.onNext(
            timeMap[Triple(
                selectedTempSubject.value,
                selectedSizeSubject.value,
                selectedTypeSubject.value
            )] ?: 0
        )
    }

    override val calculatedTimeStream: Observable<Int>
        get() = calculatedTimeSubject

    override val selectedSizeStream: Observable<SetupSize>
        get() = selectedSizeSubject

    override val selectedTypeStream: Observable<SetupType>
        get() = selectedTypeSubject

    override val selectedTemperatureStream: Observable<SetupTemperature>
        get() = selectedTempSubject
}
package com.example.eggyapp.ui.setup

import androidx.lifecycle.ViewModel
import com.example.eggyapp.base.utils.addToComposite
import com.example.eggyapp.data.SetupEggRepository
import com.example.eggyapp.data.model.SetupSize
import com.example.eggyapp.data.model.SetupTemperature
import com.example.eggyapp.data.model.SetupType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

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
        setupRepository.calculatedTimeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                calculatedTime.value = it
                isCookEnable.value = it != 0
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                selectedType.value = it
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedSize() {
        setupRepository.selectedSizeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                selectedSize.value = it
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedTemperature() {
        setupRepository.selectedTemperatureStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                selectedTemperature.value = it
            }.addToComposite(compositeDisposable)
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
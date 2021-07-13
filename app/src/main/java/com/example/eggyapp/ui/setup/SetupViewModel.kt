package com.example.eggyapp.ui.setup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eggyapp.data.SetupEggRepository
import com.example.eggyapp.data.SetupSize
import com.example.eggyapp.data.SetupTemperature
import com.example.eggyapp.data.SetupType
import com.example.eggyapp.utils.addToComposite
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
        setupRepository.postTemperature(temperature)
    }

    fun onSelectSize(size: SetupSize?) {
        setupRepository.postSize(size)
    }

    fun onSelectType(type: SetupType?) {
        setupRepository.postType(type)
    }
}
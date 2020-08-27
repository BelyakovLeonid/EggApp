package com.example.eggyapp.ui.setup

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
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val mutableCalculatedTime = MutableLiveData<Int>()
    val calculatedTime: LiveData<Int> = mutableCalculatedTime

    private val mutableSelectedTemp = MutableLiveData<SetupTemperature>()
    val selectedTemperature: LiveData<SetupTemperature> = mutableSelectedTemp

    private val mutableSelectedSize = MutableLiveData<SetupSize>()
    val selectedSize: LiveData<SetupSize> = mutableSelectedSize

    private val mutableSelectedType = MutableLiveData<SetupType>()
    val selectedType: LiveData<SetupType> = mutableSelectedType

    private val mutableIsCookEnable = MutableLiveData<Boolean>()
    val isCookEnable: LiveData<Boolean> = mutableIsCookEnable

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
                mutableCalculatedTime.value = it
                mutableIsCookEnable.value = it != 0
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableSelectedType.value = it
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedSize() {
        setupRepository.selectedSizeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableSelectedSize.value = it
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedTemperature() {
        setupRepository.selectedTemperatureStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableSelectedTemp.value = it
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
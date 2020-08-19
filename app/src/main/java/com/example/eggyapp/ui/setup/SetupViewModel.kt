package com.example.eggyapp.ui.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eggyapp.EggApp
import com.example.eggyapp.data.SetupSize
import com.example.eggyapp.data.SetupTemperature
import com.example.eggyapp.data.SetupType
import com.example.eggyapp.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class SetupViewModel : ViewModel() {

    private val setupRepository = EggApp.setupRepo //todo inject

    private val compositeDisposable = CompositeDisposable()

    private val mutableCalculatedTime = MutableLiveData<Int>()
    val calculatedTime: LiveData<Int> = mutableCalculatedTime

    private val mutableIsCookEnable = MutableLiveData<Boolean>()
    val isCookEnable: LiveData<Boolean> = mutableIsCookEnable

    init {
        observeCalculatedTime()
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableCalculatedTime.value = it
                mutableIsCookEnable.value = it != 0
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
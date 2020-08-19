package com.example.eggyapp.ui.cook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eggyapp.EggApp
import com.example.eggyapp.data.SetupType
import com.example.eggyapp.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class CookViewModel : ViewModel() {

    private val setupRepository = EggApp.setupRepo //todo inject

    private val compositeDisposable = CompositeDisposable()

    private val mutableCalculatedTime = MutableLiveData<Int>()
    val calculatedTime: LiveData<Int> = mutableCalculatedTime

    private val mutableSelectedType = MutableLiveData<SetupType>()
    val selectedType: LiveData<SetupType> = mutableSelectedType

    init {
        Log.d("MyTag", "init")
        observeCalculatedTime()
        observeSelectedType()
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableCalculatedTime.value = it
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableSelectedType.value = it
            }.addToComposite(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
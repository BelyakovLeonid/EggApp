package com.example.eggyapp.ui.cook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eggyapp.data.SetupEggRepository
import com.example.eggyapp.data.SetupType
import com.example.eggyapp.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CookViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val mutableCalculatedTime = MutableLiveData<Int>()
    val calculatedTime: LiveData<Int> = mutableCalculatedTime

    private val mutableSelectedType = MutableLiveData<SetupType>()
    val selectedType: LiveData<SetupType> = mutableSelectedType

    init {
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
package com.example.eggyapp.ui.cook

import androidx.lifecycle.ViewModel
import com.example.eggyapp.data.SetupEggRepository
import com.example.eggyapp.data.SetupType
import com.example.eggyapp.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CookViewModel @Inject constructor(
    private val setupRepository: SetupEggRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val calculatedTime = MutableStateFlow(0)
    val selectedType = MutableStateFlow(SetupType.NONE)

    init {
        observeCalculatedTime()
        observeSelectedType()
    }

    private fun observeCalculatedTime() {
        setupRepository.calculatedTimeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                calculatedTime.value = it
            }.addToComposite(compositeDisposable)
    }

    private fun observeSelectedType() {
        setupRepository.selectedTypeStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                selectedType.value = it
            }.addToComposite(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
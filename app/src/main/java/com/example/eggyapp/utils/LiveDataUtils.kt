package com.example.eggyapp.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.hadilq.liveevent.LiveEvent

inline fun <T> LifecycleOwner.observeLiveData(
    liveData: LiveData<T>,
    crossinline block: (T) -> Unit
) {
    liveData.observe(this, Observer { block.invoke(it) })
}

fun LiveEvent<Unit>.postEvent(){
    this.postValue(Unit)
}
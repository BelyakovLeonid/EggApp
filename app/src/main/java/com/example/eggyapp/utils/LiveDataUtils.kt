package com.example.eggyapp.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LifecycleOwner.observeLiveData(
    liveData: LiveData<T>,
    crossinline block: (T) -> Unit
) {
    liveData.observe(this, Observer { block.invoke(it) })
}
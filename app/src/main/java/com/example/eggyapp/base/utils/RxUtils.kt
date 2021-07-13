package com.example.eggyapp.base.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addToComposite(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}
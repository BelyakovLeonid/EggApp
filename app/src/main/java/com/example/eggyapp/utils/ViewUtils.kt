package com.example.eggyapp.utils

import android.view.View
import android.widget.Toast

fun View.dpToPx(dp: Float) = resources.displayMetrics.density * dp

val Toast?.isShowing
    get() = this?.view?.isShown == true
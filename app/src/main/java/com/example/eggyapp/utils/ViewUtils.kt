package com.example.eggyapp.utils

import android.view.View

fun View.dpToPx(dp: Int) = resources.displayMetrics.density * dp

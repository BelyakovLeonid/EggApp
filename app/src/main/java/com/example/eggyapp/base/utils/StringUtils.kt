package com.example.eggyapp.base.utils

import java.text.SimpleDateFormat
import java.util.*

fun Number.toTimerString(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)

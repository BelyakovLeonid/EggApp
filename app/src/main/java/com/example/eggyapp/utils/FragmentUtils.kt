package com.example.eggyapp.utils

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.getColor(colorId: Int): Int =
    ContextCompat.getColor(requireContext(), colorId)
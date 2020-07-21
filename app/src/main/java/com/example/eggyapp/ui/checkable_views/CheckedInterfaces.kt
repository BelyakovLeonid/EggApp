package com.example.eggyapp.ui.checkable_views

import android.widget.Checkable

interface CheckableListenable : Checkable {
    fun addOnCheckListener(listener: CheckedListener)
}

interface CheckedListener {
    fun onCheckedChanged(view: CheckableListenable, isChecked: Boolean)
}
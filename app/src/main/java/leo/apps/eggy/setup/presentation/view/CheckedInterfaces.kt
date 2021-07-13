package leo.apps.eggy.setup.presentation.view

import android.widget.Checkable

interface CheckableListenable : Checkable {
    fun addOnCheckListener(listener: CheckedListener)
    fun setIndex(index: Int)
    fun getIndex(): Int
}

interface CheckedListener {
    fun onCheckedChanged(view: CheckableListenable, index: Int, isChecked: Boolean)
}
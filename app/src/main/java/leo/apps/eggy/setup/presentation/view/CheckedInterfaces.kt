package leo.apps.eggy.setup.presentation.view

import android.view.View
import android.widget.Checkable

interface CheckableListenable : Checkable {
    fun addOnCheckListener(listener: CheckedChangedListener)
    fun setIndex(index: Int)
    fun getIndex(): Int
}

interface CheckedChangedListener {
    fun onCheckedChanged(view: CheckableListenable, index: Int, isChecked: Boolean)
}

interface CheckedIndexListener {
    fun onCheckedIndex(view: View, index: Int)
}
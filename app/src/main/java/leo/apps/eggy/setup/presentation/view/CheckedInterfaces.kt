package leo.apps.eggy.setup.presentation.view

import android.view.View
import android.widget.Checkable

interface CheckableListenable : Checkable {
    var index: Int
    fun addOnCheckListener(listener: CheckedChangedListener)
}

interface CheckedChangedListener {
    fun onCheckedChanged(view: CheckableListenable, index: Int, isChecked: Boolean)
}

interface CheckedIndexListener {
    fun onCheckedIndex(view: View, index: Int)
}
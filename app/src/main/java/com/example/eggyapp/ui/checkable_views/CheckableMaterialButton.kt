package com.example.eggyapp.ui.checkable_views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class CheckableMaterialButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr), CheckableListenable {
    private var checkedListener: CheckedListener? = null

    init {
        isCheckable = true
    }

    override fun addOnCheckListener(listener: CheckedListener) {
        checkedListener = listener
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        checkedListener?.onCheckedChanged(this, checked)
    }
}
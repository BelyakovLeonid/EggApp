package com.example.eggyapp.ui.checkable_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children

class CheckableButtonGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    CheckedListener {

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        when (child) {
            is CheckableListenable -> {
                child.addOnCheckListener(this)
            }
        }
        super.addView(child, index, params)
    }

    override fun onCheckedChanged(view: CheckableListenable, isChecked: Boolean) {
        if (isChecked) {
            children.iterator().forEach {
                (it as? CheckableListenable)?.let { childView ->
                    if (childView != view) {
                        childView.isChecked = false
                    }
                }
            }
        }
    }
}
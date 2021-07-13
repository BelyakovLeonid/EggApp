package com.example.eggyapp.base.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class NoPaddingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle,
    defStyleRes: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr, defStyleRes) {

    init {
        includeFontPadding = false
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        val ascent = paint.fontMetrics.ascent
        val descent = paint.fontMetrics.descent
        val bounds = Rect()
        text?.let { paint.getTextBounds(text.toString(), 0, text.length, bounds) }

        setPadding(0, (ascent - bounds.top).toInt(), 0, (bounds.bottom - descent).toInt())
    }
}
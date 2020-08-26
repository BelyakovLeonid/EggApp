package com.example.eggyapp.ui.views;

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import com.example.eggyapp.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.parcel.Parcelize

class ControlButton : MaterialButton {

    var onStartListener: (() -> Unit)? = null
    var onCancelListener: (() -> Unit)? = null

    private var currentState = ButtonState.STATE_IDLED
        set(value) {
            field = value
            updateText()
        }

    constructor(cnt: Context, attr: AttributeSet, defAttr: Int) : super(cnt, attr, defAttr)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    init {
        updateText()
    }

    override fun performClick(): Boolean {
        when (currentState) {
            ButtonState.STATE_IDLED -> {
                onStartListener?.invoke()
                currentState = ButtonState.STATE_STARTED
            }
            ButtonState.STATE_STARTED -> {
                onCancelListener?.invoke()
                currentState = ButtonState.STATE_IDLED
            }
        }
        return super.performClick()
    }

    fun setState(state: ButtonState) {
        currentState = state
    }

    private fun updateText() {
        text = when (currentState) {
            ButtonState.STATE_IDLED -> context.getString(R.string.cook_start)
            ButtonState.STATE_STARTED -> context.getString(R.string.cook_cancel)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)
        currentState = (state as ControlSavedState).currentState //todo NPE ?
    }

    override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState()
        return ControlSavedState(currentState)
    }

    @Parcelize
    private data class ControlSavedState(
        val currentState: ButtonState
    ) : Parcelable
}

enum class ButtonState {
    STATE_STARTED, STATE_IDLED
}
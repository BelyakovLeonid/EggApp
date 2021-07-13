package leo.apps.eggy.cook.presentation.view;

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import leo.apps.eggy.R
import com.google.android.material.button.MaterialButton
import kotlinx.parcelize.Parcelize

class ControlButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): MaterialButton(context, attrs, defStyleAttr) {

    var onStartListener: (() -> Unit)? = null
    var onCancelListener: (() -> Unit)? = null

    private var currentState = ButtonState.STATE_IDLE
        set(value) {
            field = value
            updateText()
        }

    init {
        updateText()
    }

    override fun performClick(): Boolean {
        when (currentState) {
            ButtonState.STATE_IDLE -> {
                onStartListener?.invoke()
                currentState = ButtonState.STATE_STARTED
            }
            ButtonState.STATE_STARTED -> {
                onCancelListener?.invoke()
                currentState = ButtonState.STATE_IDLE
            }
        }
        return super.performClick()
    }

    fun setState(state: ButtonState) {
        currentState = state
    }

    private fun updateText() {
        text = when (currentState) {
            ButtonState.STATE_IDLE -> context.getString(R.string.cook_start)
            ButtonState.STATE_STARTED -> context.getString(R.string.cook_cancel)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)
        currentState = (state as ControlSavedState).currentState
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
    STATE_STARTED, STATE_IDLE
}
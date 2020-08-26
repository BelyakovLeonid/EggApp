package com.example.eggyapp.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.eggyapp.utils.toTimerString
import kotlinx.android.parcel.Parcelize

private const val ANIMATION_DURATION = 1000L

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private var currentTime: Int = 0
    private var animator: ValueAnimator? = null

    init {
        text = 0.toTimerString()
    }

    fun setTime(time: Int) {
        if (time != currentTime) {
            animator?.cancel()
            animator = null
            animateTimeChanged(time)
        }
    }

    private fun animateTimeChanged(newTime: Int) {
        animator = ValueAnimator.ofInt(currentTime, newTime).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                text = (it.animatedValue as Int).toTimerString()
            }
        }

        animator?.start()
        currentTime = newTime
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)
        currentTime = (state as SavedState).currentTime
        text = currentTime.toTimerString()
    }

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState()
        return SavedState(currentTime)
    }

    @Parcelize
    private data class SavedState(
        val currentTime: Int
    ) : Parcelable
}
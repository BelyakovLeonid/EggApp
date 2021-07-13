package com.example.eggyapp.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateInterpolator
import com.example.eggyapp.utils.toTimerString

private const val ANIMATION_DURATION = 600L

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private var currentTime: Int = NO_VALUE_SET
    private var animator: ValueAnimator? = null

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
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                text = (it.animatedValue as Int).toTimerString()
            }
            start()
        }

        currentTime = newTime
    }

    private companion object{
        const val NO_VALUE_SET = -1
    }
}
package leo.apps.eggy.setup.presentation.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import leo.apps.eggy.base.utils.toTimerString

private const val ANIMATION_DURATION = 600L

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private var animator: ValueAnimator? = null
    private var currentTime: Int? = null

    fun setTime(newTime: Int) {
        if (newTime != currentTime) {
            animateTimeChanged(currentTime ?: 0, newTime)
            currentTime = newTime
        }
    }

    private fun animateTimeChanged(fromTime: Int, newTime: Int) {
        animator?.cancel()
        animator = ValueAnimator.ofInt(fromTime, newTime).apply {
            duration = ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                text = (it.animatedValue as Int).toTimerString()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        animator = null
        super.onDetachedFromWindow()
    }
}
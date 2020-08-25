package com.example.eggyapp.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.eggyapp.R
import kotlinx.android.synthetic.main.v_clock.view.*
import java.util.*
import kotlin.math.abs

private const val ANIMATION_DURATION = 1500L

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val digitViews: List<ClockDigitView>
    private var currentTime: Int = 0
    private var animator: ValueAnimator? = null

    init {
        View.inflate(context, R.layout.v_clock, this)
        digitViews = listOf(digit1, digit2, digit3, digit4)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ClockView,
            defStyleAttr, 0
        ).apply {
            try {
                val digitSize = getDimension(R.styleable.ClockView_textSize, 30f)
                digitViews.forEach { it.setTextSize(digitSize) }
                colon.textSize = digitSize
            } finally {
                recycle()
            }
        }
    }

    fun setTime(time: Int) {
        if (time != currentTime) {
            animator?.cancel()
            animator = null
            animateTimeChanged(time)
        }
    }

    private fun animateTimeChanged(newTime: Int) {
        digitViews.forEach { it.setDirection(newTime > currentTime) }
        digitViews[0].setAnimationDuration(calculateAnimDuration(newTime, 600_000))
        digitViews[1].setAnimationDuration(calculateAnimDuration(newTime, 60_000))
        digitViews[2].setAnimationDuration(calculateAnimDuration(newTime, 10_000))
        digitViews[3].setAnimationDuration(calculateAnimDuration(newTime, 1_000))

        animator = ValueAnimator.ofInt(currentTime, newTime)
        animator?.duration = ANIMATION_DURATION
        animator?.addUpdateListener {
            val time = it.animatedValue as Int
            val calendar = Calendar.getInstance().apply { timeInMillis = time.toLong() }

            digitViews[0].setDigit(calendar.get(Calendar.MINUTE).div(10))
            digitViews[1].setDigit(calendar.get(Calendar.MINUTE).rem(10))
            digitViews[2].setDigit(calendar.get(Calendar.SECOND).div(10))
            digitViews[3].setDigit(calendar.get(Calendar.SECOND).rem(10))
        }
        animator?.start()
    }

    private fun calculateAnimDuration(time: Int, interval: Long): Long {
        val delta = abs(currentTime - time)
        val res = if (interval >= 60_000) {
            if (currentTime > time) {
                (delta + (60_000 - currentTime.rem(60_000))).div(interval)
            } else {
                (delta + currentTime.rem(60_000)).div(interval)
            }
        } else {
            delta.div(interval)
        }

        Log.d(
            "MyTag",
            "time = $time, interval = $interval, res = $res, res2 = ${ANIMATION_DURATION / res}"
        )
        return ANIMATION_DURATION / res
    }
}
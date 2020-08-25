package com.example.eggyapp.ui.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnStart
import com.example.eggyapp.utils.dpToPx
import kotlin.math.min

class ClockDigitView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var animatorSet: AnimatorSet? = null

    private var textHeight: Int = 0
    private var textWidth: Int = 0
    private var currentTextPositionX = 0f
    private var currentTextPositionY = 0f
    private var showingDigit: Int = 0
    private var currentDigit: Int = 0
    private var animationDuration: Long = 0
    private var directionForward: Boolean = true

    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val requestedWidth = MeasureSpec.getSize(widthMeasureSpec)
        val requestedWidthMode = MeasureSpec.getMode(widthMeasureSpec)

        val requestedHeight = MeasureSpec.getSize(heightMeasureSpec)
        val requestedHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        val desiredWidth = textWidth * 5 / 4
        val desiredHeight = textHeight * 5 / 3

        val width = when (requestedWidthMode) {
            MeasureSpec.EXACTLY -> requestedWidth
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> min(requestedWidth, desiredWidth)
        }

        val height = when (requestedHeightMode) {
            MeasureSpec.EXACTLY -> requestedHeight
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> min(requestedHeight, desiredHeight)
        }

        currentTextPositionY = height / 2f + textHeight / 2f
        currentTextPositionX = width / 2f - textWidth / 2f

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawText(showingDigit.toString(), currentTextPositionX, currentTextPositionY, paint)
        super.onDraw(canvas)
    }

    fun setDigit(digit: Int) {
        if (currentDigit != digit) {
            animatorSet?.cancel()
            animatorSet = null
            animateDigitChange(digit)
        }
    }

    private fun animateDigitChange(digit: Int) {
        val animators = mutableListOf<Animator>()
        val centerTextPosition = (measuredHeight / 2f) + (textHeight / 2f)
        val startPosition: Float
        val endPosition: Float
        val intermediateDigits: IntProgression

        if (showingDigit < digit) {
            startPosition = measuredHeight.toFloat() + textHeight.toFloat()
            endPosition = 0f
            intermediateDigits = (showingDigit + 1) until digit
        } else {
            startPosition = 0f
            endPosition = measuredHeight.toFloat() + textHeight.toFloat()
            intermediateDigits = (showingDigit - 1) downTo (digit + 1)
        }

        animators.add(
            ValueAnimator.ofFloat(currentTextPositionY, endPosition)
                .apply {
                    duration = animationDuration / 2
                    interpolator = AccelerateInterpolator()
                    addUpdateListener {
                        currentTextPositionY = it.animatedValue as Float
                        invalidate()
                    }
                }
        )

        for (i in intermediateDigits) {
            animators.add(
                ValueAnimator.ofFloat(startPosition, endPosition)
                    .apply {
                        duration = animationDuration
                        doOnStart { showingDigit = i }
                        addUpdateListener {
                            currentTextPositionY = it.animatedValue as Float
                            invalidate()
                        }
                    }
            )
        }

        animators.add(
            ValueAnimator.ofFloat(startPosition, centerTextPosition).apply {
                duration = animationDuration / 2
                doOnStart { showingDigit = digit }
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    currentTextPositionY = it.animatedValue as Float
                    invalidate()
                }
            }
        )

        animatorSet = AnimatorSet()
        animatorSet?.playSequentially(animators)
        animatorSet?.start()

        currentDigit = digit
    }

    fun setTextSize(sizeSp: Float) {
        val rect = Rect()
        paint.textSize = dpToPx(sizeSp)
        paint.getTextBounds(showingDigit.toString(), 0, 1, rect)

        textWidth = paint.measureText(showingDigit.toString()).toInt()
        textHeight = rect.height()
        requestLayout()
    }

    fun setDirection(isForward: Boolean) {
        directionForward = isForward
    }

    fun setAnimationDuration(duration: Long) {
        animationDuration = duration
    }
}
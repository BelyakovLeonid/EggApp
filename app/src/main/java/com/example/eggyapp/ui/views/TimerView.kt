package com.example.eggyapp.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.eggyapp.R
import com.example.eggyapp.utils.dpToPx
import com.example.eggyapp.utils.getBitmap
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val RADIUS_RATIO = 0.7f
private const val GRADIENT_END_OFFSET = 0.1f
private const val ANIMATION_DURATION = 1000L

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var animator: ValueAnimator? = null

    private val arcRectangle = RectF()
    private var background = context.getBitmap(R.drawable.timer_background)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        paint.style = Paint.Style.FILL
        paint.typeface = resources.getFont(R.font.monsterrat_bold)
    }

    private var bigRadius: Float = 0f
    private var smallRadius: Float = 0f
    private var gradient: SweepGradient? = null

    private var currentDegree = 0f
    private var currentText = "0:00"
    private var alwaysText = " MIN"

    private val gradientStartColor = ContextCompat.getColor(context, R.color.colorGraySuperLight)
    private val gradientEndColor = ContextCompat.getColor(context, R.color.colorPrimaryLight)
    private val emptyTimerColor = ContextCompat.getColor(context, R.color.colorAlmostWhite)
    private val timerTitleColor = ContextCompat.getColor(context, R.color.colorBlack)
    private val timerSubTitleColor = ContextCompat.getColor(context, R.color.colorGrayLight)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bigRadius = min(w, h) / 2f
        smallRadius = RADIUS_RATIO * bigRadius
        background = Bitmap.createScaledBitmap(
            background, (bigRadius * 2).toInt(), (bigRadius * 2).toInt(), true
        )
        arcRectangle.left = bigRadius - smallRadius
        arcRectangle.top = bigRadius - smallRadius
        arcRectangle.right = bigRadius + smallRadius
        arcRectangle.bottom = bigRadius + smallRadius
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(background, 0f, 0f, paint)
        drawEmptyTimerArc(canvas)
        drawText(canvas)
        drawProgressTimerArc(canvas)
        drawProgressCircle(canvas)
    }

    private fun drawEmptyTimerArc(canvas: Canvas?) {
        paint.shader = null
        paint.color = emptyTimerColor
        paint.strokeWidth = dpToPx(10f) //todo make width adaptive to size
        paint.style = Paint.Style.STROKE
        canvas?.drawArc(arcRectangle, 0f, 360f, false, paint)
    }

    private fun drawText(canvas: Canvas?) {
        textPaint.color = timerTitleColor
        textPaint.textSize = dpToPx(28f) //todo need sp? //todo adapt to size
        textPaint.textAlign = Paint.Align.CENTER
        val length = textPaint.measureText(currentText)
        canvas?.drawText(
            currentText,
            bigRadius,
            bigRadius - (smallRadius / 3),
            textPaint
        )

        textPaint.color = timerSubTitleColor
        textPaint.textSize = dpToPx(12f) //todo need sp? //todo adapt to size
        textPaint.textAlign = Paint.Align.LEFT
        canvas?.drawText(
            alwaysText,
            bigRadius + length / 2,
            bigRadius - (smallRadius / 3),
            textPaint
        )
    }

    private fun drawProgressTimerArc(canvas: Canvas?) {
        paint.shader = gradient
        paint.style = Paint.Style.STROKE
        canvas?.rotate(-90f, bigRadius, bigRadius)
        canvas?.drawArc(arcRectangle, 0f, currentDegree, false, paint)
    }

    private fun drawProgressCircle(canvas: Canvas?) {
        paint.shader = null
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas?.drawCircle(
            (bigRadius + cos((currentDegree / 180f) * Math.PI) * smallRadius).toFloat(),
            (bigRadius + sin((currentDegree / 180f) * Math.PI) * smallRadius).toFloat(),
            dpToPx(6f),
            paint
        )

        paint.color = Color.WHITE
        canvas?.drawCircle(
            (bigRadius + cos((currentDegree / 180f) * Math.PI) * smallRadius).toFloat(),
            (bigRadius + sin((currentDegree / 180f) * Math.PI) * smallRadius).toFloat(),
            dpToPx(3f),
            paint
        )
    }

    fun setProgress(progress: Float) {
        animator?.cancel()
        animator = null
        currentDegree = progress * 360f

        val gradientEndPosition = (currentDegree / 360f) - GRADIENT_END_OFFSET
        gradient = SweepGradient(
            bigRadius,
            bigRadius,
            intArrayOf(gradientStartColor, gradientEndColor),
            floatArrayOf(0f, gradientEndPosition)
        )

        invalidate()
    }

    fun setTimerText(timerString: String) {
        currentText = timerString
        invalidate()
    }

    fun dropProgress(delay: Long = 0) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(currentDegree, 0f)
        animator?.duration = (ANIMATION_DURATION * currentDegree / 360f).toLong()
        animator?.startDelay = delay
        animator?.addUpdateListener {
            val value = it.animatedValue as Float
            currentDegree = value
            invalidate()
        }
        animator?.start()
    }
}
package leo.apps.eggy.cook.presentation.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.use
import leo.apps.eggy.R
import leo.apps.eggy.base.utils.dpToPx
import leo.apps.eggy.base.utils.getBitmap
import leo.apps.eggy.base.utils.toRadians
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var animator: ValueAnimator? = null

    private val arcRectangle = RectF()
    private var background = context.getBitmap(R.drawable.timer_background)

    private var currentDegree = MIN_DEGREE
    private var bigRadius: Float = 0f
    private var smallRadius: Float = 0f
    private var progressBigRadius: Float = 0f
    private var progressSmallRadius: Float = 0f
    private var gradient: SweepGradient? = null

    private var digitsText = ""
    private var minutesText = resources.getString(R.string.timer_minutes)

    private val gradientStartColor = context.getColor(R.color.colorGraySuperLight)
    private val gradientEndColor = context.getColor(R.color.colorPrimaryLight)
    private val emptyTimerColor = context.getColor(R.color.colorAlmostWhite)
    private val progressColor = context.getColor(R.color.colorPrimary)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = dpToPx(EMPTY_ARC_WIDTH_DP)
    }
    private val digitsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorBlack)
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.monsterrat_bold)
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorGrayLight)
        textAlign = Paint.Align.LEFT
        typeface = ResourcesCompat.getFont(context, R.font.monsterrat_bold)
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TimerView,
            defStyleAttr, 0
        ).use { a ->
            digitsPaint.textSize = a.getDimension(
                R.styleable.TimerView_digitsTextSize,
                dpToPx(DEFAULT_DIGITS_TEXT_SIZE_DP)
            )
            textPaint.textSize = a.getDimension(
                R.styleable.TimerView_minutesTextSize,
                dpToPx(DEFAULT_MINUTES_TEXT_SIZE_DP)
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val minDimension = min(w, h)
        bigRadius = minDimension / 2f
        smallRadius = RADIUS_RATIO * bigRadius
        progressBigRadius = PROGRESS_RADIUS_RATIO * smallRadius
        progressSmallRadius = PROGRESS_STROKE_RATIO * progressBigRadius
        background = Bitmap.createScaledBitmap(background, minDimension, minDimension, true)
        arcRectangle.set(
            bigRadius - smallRadius,
            bigRadius - smallRadius,
            bigRadius + smallRadius,
            bigRadius + smallRadius
        )
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        animator = null
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(background, 0f, 0f, paint)
        drawEmptyTimerArc(canvas)
        drawText(canvas)
        drawProgressTimerArc(canvas)
        drawProgressCircle(canvas)
    }

    private fun drawEmptyTimerArc(canvas: Canvas) {
        paint.shader = null
        paint.color = emptyTimerColor
        paint.style = Paint.Style.STROKE
        canvas.drawArc(arcRectangle, MIN_DEGREE, MAX_DEGREE, false, paint)
    }

    private fun drawText(canvas: Canvas) {
        val length = digitsPaint.measureText(digitsText)
        canvas.drawText(
            digitsText,
            bigRadius,
            bigRadius - (smallRadius / 3),
            digitsPaint
        )

        canvas.drawText(
            minutesText,
            bigRadius + length / 2,
            bigRadius - (smallRadius / 3),
            textPaint
        )
    }

    private fun drawProgressTimerArc(canvas: Canvas) {
        paint.shader = gradient
        paint.style = Paint.Style.STROKE
        canvas.rotate(ROTATION_DEGREE, bigRadius, bigRadius)
        canvas.drawArc(arcRectangle, MIN_DEGREE, currentDegree, false, paint)
    }

    private fun drawProgressCircle(canvas: Canvas?) {
        val centerX = (bigRadius + cos(currentDegree.toRadians()) * smallRadius)
        val centerY = (bigRadius + sin(currentDegree.toRadians()) * smallRadius)

        paint.shader = null
        paint.color = progressColor
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas?.drawCircle(centerX, centerY, progressBigRadius, paint)

        paint.color = Color.WHITE
        canvas?.drawCircle(centerX, centerY, progressSmallRadius, paint)
    }

    fun setProgress(progress: Float) {
        currentDegree = progress * MAX_DEGREE

        val gradientEndPosition = (currentDegree / MAX_DEGREE) - GRADIENT_END_OFFSET
        gradient = SweepGradient(
            bigRadius,
            bigRadius,
            intArrayOf(gradientStartColor, gradientEndColor),
            floatArrayOf(MIN_DEGREE, gradientEndPosition)
        )

        invalidate()
    }

    fun setTimerText(timerString: String) {
        digitsText = timerString
        invalidate()
    }

    fun dropProgress(delay: Long = 0) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(currentDegree, MIN_DEGREE).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                currentDegree = value
                invalidate()
            }
            duration = (ANIMATION_DURATION * currentDegree / MAX_DEGREE).toLong()
            startDelay = delay
            start()
        }
    }

    private companion object {
        const val RADIUS_RATIO = 0.7f
        const val PROGRESS_RADIUS_RATIO = 0.05f
        const val PROGRESS_STROKE_RATIO = 0.4f
        const val GRADIENT_END_OFFSET = 0.1f

        const val MIN_DEGREE = 0f
        const val MAX_DEGREE = 360f
        const val ROTATION_DEGREE = -90f

        const val ANIMATION_DURATION = 1000L

        const val EMPTY_ARC_WIDTH_DP = 10
        const val DEFAULT_DIGITS_TEXT_SIZE_DP = 26
        const val DEFAULT_MINUTES_TEXT_SIZE_DP = 12
    }
}
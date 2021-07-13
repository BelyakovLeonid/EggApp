package leo.apps.eggy.base.utils

import leo.apps.eggy.R
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

private const val ANIMATION_DURATION = 2000L
private const val CONFETTI_START_ANGLE = 0.0
private const val CONFETTI_END_ANGLE = 359.0
private const val CONFETTI_PIECES = 150
private const val CONFETTI_PIECES_SIZE_DP = 12
private const val SPEED_MIN = 1F
private const val SPEED_MAX = 5F

fun KonfettiView.makeDefaultConfetti(){
    this.build()
        .addColors(
            context.getColor(R.color.confetti_yellow),
            context.getColor(R.color.confetti_orange),
            context.getColor(R.color.confetti_purple),
            context.getColor(R.color.confetti_pink)
        )
        .setDirection(CONFETTI_START_ANGLE, CONFETTI_END_ANGLE)
        .setSpeed(SPEED_MIN, SPEED_MAX)
        .setFadeOutEnabled(true)
        .setTimeToLive(ANIMATION_DURATION)
        .addShapes(Shape.Square, Shape.Circle)
        .addSizes(Size(CONFETTI_PIECES_SIZE_DP))
        .setPosition(
            0f,
            width.toFloat(),
            -dpToPx(CONFETTI_PIECES_SIZE_DP),
            -dpToPx(CONFETTI_PIECES_SIZE_DP)
        )
        .streamFor(CONFETTI_PIECES, ANIMATION_DURATION)
}

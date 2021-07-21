package leo.apps.eggy.timer.model

import leo.apps.eggy.base.utils.toTimerString

data class TimerServiceState(
    val isRunning: Boolean,
    val progress: Float,
    val timerText: String
) {

    companion object {
        val DEFAULT = TimerServiceState(
            isRunning = false,
            progress = 0f,
            timerText = 0.toTimerString()
        )
    }
}
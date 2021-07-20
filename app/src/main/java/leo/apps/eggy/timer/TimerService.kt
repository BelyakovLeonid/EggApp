package leo.apps.eggy.timer

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import leo.apps.eggy.R
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.utils.toTimerString

class TimerService : Service() {

    private var notificationHelper: TimerNotificationHelper? = null
    private val timerBinder = TimerBinder()
    private var timer: CountDownTimer? = null

    private var millisInFuture: Long = 0
        set(value) {
            mutableProgress.value = 0f
            mutableTimerText.value = value.toTimerString()
            field = value
        }

    private val mutableProgress = MutableStateFlow(0F)
    private val mutableTimerText = MutableStateFlow(0.toTimerString())

    private val finishEvent = Channel<Unit>(Channel.BUFFERED)
    private val cancelEvent = Channel<Unit>(Channel.BUFFERED)

    override fun onCreate() {
        super.onCreate()
        notificationHelper = TimerNotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra(ACTION_CANCEL, false) == true) {
            stopTimer()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return timerBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

    private fun startTimer() {
        notificationHelper?.cancelNotification()

        timer = object : CountDownTimer(millisInFuture, 10) {
            override fun onFinish() {
                timer = null
                stopForeground(true)
                notificationHelper?.notifyFinish()
                finishEvent.trySend(Unit)
                mutableTimerText.value = millisInFuture.toTimerString()
            }

            override fun onTick(millisUntilEnd: Long) {
                val progress = 1 - millisUntilEnd / millisInFuture.toFloat()
                val timerString = millisUntilEnd.toTimerString()
                mutableProgress.value = progress
                mutableTimerText.value = timerString
                notifyProgress(progress, timerString)
            }
        }
        timer?.start()
    }

    private fun notifyProgress(progress: Float, timerString: String) {
        val notification = notificationHelper?.createProgressNotification(
            progress = (progress * MAX_PROGRESS).toInt(),
            text = getString(R.string.timer_notif_subtitle, timerString),
        )

        startForeground(FOREGROUND_ID, notification)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        stopForeground(true)
        cancelEvent.trySend(Unit)
        mutableTimerText.value = millisInFuture.toTimerString()
    }

    inner class TimerBinder : Binder() {
        val progress: Flow<Float>
            get() = this@TimerService.mutableProgress.asStateFlow()

        val timerText: Flow<String>
            get() = this@TimerService.mutableTimerText.asStateFlow()

        val finish: Flow<Unit>
            get() = this@TimerService.finishEvent.receiveAsFlow()

        val cancel: Flow<Unit>
            get() = this@TimerService.cancelEvent.receiveAsFlow()

        val isRunning: Boolean
            get() = this@TimerService.timer != null

        fun startTimer() = this@TimerService.startTimer()

        fun stopTimer() = this@TimerService.stopTimer()

        fun setTime(millisInFuture: Long) {
            this@TimerService.millisInFuture = millisInFuture
        }

        fun setType(type: SetupType?) {
            this@TimerService.notificationHelper?.setEggType(type)
        }
    }

    companion object {
        const val ACTION_CANCEL = "leo_apps_eggy_action_cancel"
        const val MAX_PROGRESS = 1000
        private const val FOREGROUND_ID = 1
    }
}
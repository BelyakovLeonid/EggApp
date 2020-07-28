package com.example.eggyapp.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.MutableLiveData
import com.example.eggyapp.R
import com.example.eggyapp.utils.getBitmap
import com.example.eggyapp.utils.toTimerString

private const val NOTIFICATION_CHANNEL_ID = "eggyapp"
private const val MAX_PROGRESS = 1000

class TimerService : Service() {

    private var timer: CountDownTimer? = null
    private val timerBinder = TimerBinder()

    private val progressMutableLiveData = MutableLiveData<ProgressInformation>()
    val progressLiveData = progressMutableLiveData

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("MyTag", "onBind")
        return timerBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        Log.d("MyTag", "onDestroy")
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.timer_notification_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.description = getString(R.string.timer_notification_description)

        val manager: NotificationManager? = getSystemService()
        manager?.createNotificationChannel(channel)
    }

    private fun startTimer(millisInFuture: Long, eggType: EggType) {
        timer = object : CountDownTimer(millisInFuture, 10) {
            override fun onFinish() {
                Log.d("MyTag", "onFinish")
            }

            override fun onTick(millisUntilEnd: Long) {
                val progress = 1 - millisUntilEnd / millisInFuture.toFloat()
                val progressInfo = ProgressInformation(progress, millisUntilEnd.toTimerString())
                progressMutableLiveData.value = progressInfo
                notifyProgress(progressInfo, eggType)
            }
        }
        timer?.start()
    }

    private fun notifyProgress(progressInfo: ProgressInformation, eggType: EggType) {
        val currentProgress = (progressInfo.currentProgress * MAX_PROGRESS).toInt()
        val largeIcon = getBitmap(eggType.imageResId)
        val contentText = getString(R.string.timer_notification_subtitle, progressInfo.timerString)

        val notification = Notification.Builder(this@TimerService, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_back)
            .setLargeIcon(largeIcon)
            .setContentTitle(eggType.name)
            .setContentText(contentText)
            .setCategory(Notification.CATEGORY_PROGRESS)
            .setProgress(MAX_PROGRESS, currentProgress, false)
            .build()

        startForeground(1, notification)
    }

    private fun stopTimer() {
        timer?.cancel()
        stopForeground(true)
    }

    inner class TimerBinder : Binder() {
        fun getProgressLiveData() = progressLiveData

        fun startTimer(millisInFuture: Long, eggType: EggType) =
            this@TimerService.startTimer(millisInFuture, eggType)

        fun pauseTimer() {}
        fun stopTimer() = this@TimerService.stopTimer()

    }
}

data class ProgressInformation(
    val currentProgress: Float,
    val timerString: String
)

data class EggType(
    val name: String,
    val imageResId: Int
)
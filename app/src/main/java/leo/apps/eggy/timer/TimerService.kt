package leo.apps.eggy.timer

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.content.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import leo.apps.eggy.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.utils.getBitmap
import leo.apps.eggy.base.utils.toTimerString

private const val NOTIF_PROGRESS_CHANNEL_ID = "progress_channel"
private const val NOTIF_FINISH_CHANNEL_ID = "finish_channel"
private const val ACTION_CANCEL = "action_cancel"
private const val MAX_PROGRESS = 1000
private const val FOREGROUND_ID = 1
private const val NOTIFICATION_ID = 2

class TimerService : Service() {

    private var manager: NotificationManager? = null
    private val timerBinder = TimerBinder()
    private var timer: CountDownTimer? = null
    private var action: Notification.Action? = null
    private var intent: PendingIntent? = null

    private var eggType: SetupType? = null
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

    private val notificationIcon: Bitmap
        get() = when (eggType) {
            SetupType.SOFT -> getBitmap(R.drawable.egg_soft)
            SetupType.MEDIUM -> getBitmap(R.drawable.egg_medium)
            else -> getBitmap(R.drawable.egg_hard)
        }

    private val notificationTitle: String
        get() = when (eggType) {
            SetupType.SOFT -> getString(R.string.timer_notif_soft)
            SetupType.MEDIUM -> getString(R.string.timer_notif_medium)
            else -> getString(R.string.timer_notif_hard)
        }

    override fun onCreate() {
        super.onCreate()
        manager = getSystemService()
        action = setupAction()
        intent = setupIntent()
        createNotifProgressChannel()
        createNotifFinishChannel()
    }

    private fun setupAction(): Notification.Action? {
        val actionIcon = Icon.createWithResource(this, R.drawable.ic_cancel)
        val actionText = getString(R.string.cancel)
        val intent = Intent(this, TimerService::class.java).putExtra(ACTION_CANCEL, true)
        val pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        return Notification.Action.Builder(actionIcon, actionText, pendingIntent).build()
    }

    private fun setupIntent(): PendingIntent? {
        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.cookFragment)
            .createPendingIntent()
    }

    private fun createNotifProgressChannel() {
        createChannel(
            NOTIF_PROGRESS_CHANNEL_ID,
            getString(R.string.timer_notif_progress_name),
            getString(R.string.timer_notif_progress_description),
            NotificationManager.IMPORTANCE_LOW
        )
    }

    private fun createNotifFinishChannel() {
        createChannel(
            NOTIF_FINISH_CHANNEL_ID,
            getString(R.string.timer_notif_name),
            getString(R.string.timer_notif_description),
            NotificationManager.IMPORTANCE_HIGH
        )
    }

    private fun createChannel(id: String, name: String, desc: String, importance: Int) {
        val channel = NotificationChannel(id, name, importance)
        channel.setShowBadge(false)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.description = desc
        manager?.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra(ACTION_CANCEL, false) == true) {
            stopTimer()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return timerBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

    private fun startTimer() {
        manager?.cancel(NOTIFICATION_ID)

        timer = object : CountDownTimer(millisInFuture, 10) {
            override fun onFinish() {
                timer = null
                stopForeground(true)
                notifyFinish()
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

    private fun notifyFinish() {
        val finishTitle = getString(R.string.timer_notif_finish_title)
        val finishText = getString(R.string.timer_notif_finish_text)
        val notification = buildBaseNotification(NOTIF_FINISH_CHANNEL_ID, finishText, finishTitle)
            .setCategory(Notification.CATEGORY_EVENT)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .build()

        manager?.notify(NOTIFICATION_ID, notification)
    }

    private fun notifyProgress(progress: Float, timerString: String) {
        val currentProgress = (progress * MAX_PROGRESS).toInt()
        val contentText = getString(R.string.timer_notif_subtitle, timerString)
        val notification = buildBaseNotification(NOTIF_PROGRESS_CHANNEL_ID, contentText)
            .setCategory(Notification.CATEGORY_PROGRESS)
            .setProgress(MAX_PROGRESS, currentProgress, false)
            .addAction(action)
            .build()

        startForeground(FOREGROUND_ID, notification)
    }

    private fun buildBaseNotification(
        channelId: String,
        text: String,
        title: String? = notificationTitle
    ) = Notification.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_timer_gray)
        .setLargeIcon(notificationIcon)
        .setContentTitle(title)
        .setContentText(text)
        .setContentIntent(intent)

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

        fun setType(type: SetupType) {
            this@TimerService.eggType = type
        }
    }
}
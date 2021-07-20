package leo.apps.eggy.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.IconCompat
import androidx.navigation.NavDeepLinkBuilder
import leo.apps.eggy.R
import leo.apps.eggy.base.data.model.SetupType
import leo.apps.eggy.base.utils.getBitmap


class TimerNotificationHelper(
    private val context: Context
) {

    private val manager: NotificationManager? = context.getSystemService()
    private val intent = setupIntent()
    private val action = setupAction()

    private var eggType: SetupType? = null

    private val notificationIcon: Bitmap
        get() = when (eggType) {
            SetupType.SOFT -> context.getBitmap(R.drawable.egg_soft)
            SetupType.MEDIUM -> context.getBitmap(R.drawable.egg_medium)
            else -> context.getBitmap(R.drawable.egg_hard)
        }

    private val notificationTitle: String
        get() = when (eggType) {
            SetupType.SOFT -> context.getString(R.string.timer_notif_soft)
            SetupType.MEDIUM -> context.getString(R.string.timer_notif_medium)
            else -> context.getString(R.string.timer_notif_hard)
        }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationProgressChannel()
            createNotificationFinishChannel()
        }
    }

    fun setEggType(type: SetupType?){
        eggType = type
    }

    fun cancelNotification(){
        manager?.cancel(NOTIFICATION_ID)
    }

    fun createProgressNotification(
        progress: Int,
        text: String,
    ): Notification {
        return buildBaseNotification(NOTIF_PROGRESS_CHANNEL_ID, text)
            .setCategory(Notification.CATEGORY_PROGRESS)
            .setProgress(TimerService.MAX_PROGRESS, progress, false)
            .addAction(action)
            .build()
    }

    fun notifyFinish(){
        val finishTitle = context.getString(R.string.timer_notif_finish_title)
        val finishText = context.getString(R.string.timer_notif_finish_text)
        val notification = buildBaseNotification(NOTIF_FINISH_CHANNEL_ID, finishText, finishTitle)
            .setCategory(Notification.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        manager?.notify(NOTIFICATION_ID, notification)
    }

    private fun buildBaseNotification(
        channelId: String,
        text: String,
        title: String? = notificationTitle
    ) = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_timer_gray)
        .setLargeIcon(notificationIcon)
        .setContentTitle(title)
        .setContentText(text)
        .setContentIntent(intent)

    private fun setupIntent(): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.cookFragment)
            .createPendingIntent()
    }

    private fun setupAction(): NotificationCompat.Action {
        val actionIcon = IconCompat.createWithResource(context, R.drawable.ic_cancel)
        val actionText = context.getString(R.string.cancel)

        val intent = Intent(
            context,
            TimerService::class.java
        ).putExtra(TimerService.ACTION_CANCEL, true)

        val pendingIntent = PendingIntent.getService(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        return NotificationCompat.Action.Builder(actionIcon, actionText, pendingIntent).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationProgressChannel() {
        createChannel(
            NOTIF_PROGRESS_CHANNEL_ID,
            context.getString(R.string.timer_notif_progress_name),
            context.getString(R.string.timer_notif_progress_description),
            NotificationManager.IMPORTANCE_LOW
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationFinishChannel() {
        createChannel(
            NOTIF_FINISH_CHANNEL_ID,
            context.getString(R.string.timer_notif_name),
            context.getString(R.string.timer_notif_description),
            NotificationManager.IMPORTANCE_HIGH
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String, name: String, desc: String, importance: Int) {
        val channel = NotificationChannel(id, name, importance)
        channel.setShowBadge(false)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.description = desc
        manager?.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIF_PROGRESS_CHANNEL_ID = "progress_channel"
        private const val NOTIF_FINISH_CHANNEL_ID = "finish_channel"
        private const val NOTIFICATION_ID = 2
    }
}
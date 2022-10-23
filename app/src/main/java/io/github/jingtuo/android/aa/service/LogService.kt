package io.github.jingtuo.android.aa.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import io.github.jingtuo.android.aa.MainActivity
import io.github.jingtuo.android.aa.MyApp
import io.github.jingtuo.android.aa.R
import io.github.jingtuo.android.aa.repo.LogRepo
import io.github.jingtuo.android.aa.worker.LogCatWorker

fun createNotification(
    context: Context, operate: String, content: String, actionIconId: Int, actionTitle: String
): Notification {
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val startIntent = Intent(LogService.ACTION_LOGCAT).apply {
        putExtra(LogService.KEY_OPERATE, operate)
    }
    val mainIntent = Intent(context, MainActivity::class.java)
    val contentIntent = PendingIntent.getActivity(context, 0, mainIntent, flags)
    val pIntent = PendingIntent.getBroadcast(context, 1, startIntent, flags)
    return NotificationCompat.Builder(context, MyApp.CHANNEL_ID_LOG)
        .setSmallIcon(R.drawable.ic_baseline_downloading_24)
        .setContentTitle(context.getString(R.string.log_service))
        .setContentText(content)
        .setAutoCancel(false)
        .setOngoing(true)
        .setContentIntent(contentIntent)
        .addAction(actionIconId, actionTitle, pIntent)
        .build()
}


class LogService : Service() {

    private var receiver: LogReceiver? = null

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        const val ACTION_LOGCAT = "io.github.jingtuo.android.aa.action.LOGCAT"
        const val KEY_OPERATE = "operate"
        const val OPERATE_START = "start"
        const val OPERATE_STOP = "stop"
        const val NOTIFICATION_ID = 0x0915
        const val TAG_LOGCAT = "logcat"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            stopSelf()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_REMOVE)
            }
            return START_STICKY
        }
        val notificationManager = NotificationManagerCompat.from(this)
        if (notificationManager.areNotificationsEnabled()) {
            val notification = createNotification(
                this,
                OPERATE_START, "",
                R.drawable.ic_baseline_play_arrow_24, getString(R.string.start_collect_logs)
            )
            notificationManager.notify(NOTIFICATION_ID, notification)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID, notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
            receiver = LogReceiver()
            val filter = IntentFilter()
            filter.addAction(ACTION_LOGCAT)
            registerReceiver(receiver, filter)
        }
        return START_NOT_STICKY
    }

    class LogReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null) {
                return
            }
            val operate = intent.getStringExtra(KEY_OPERATE)
            if (OPERATE_STOP == operate) {
                //停止收集日志
                val workManager = WorkManager.getInstance(context)
                workManager.cancelAllWorkByTag(TAG_LOGCAT)
                workManager.pruneWork()
                //更新通知
                val notificationManager = NotificationManagerCompat.from(context)
                val notification = createNotification(
                    context,
                    OPERATE_START,
                    "",
                    R.drawable.ic_baseline_play_arrow_24,
                    context.getString(R.string.start_collect_logs)
                )
                notificationManager.notify(NOTIFICATION_ID, notification)
            } else {
                //检测是否在日志页面
                if (MyApp.instance().inLogList) {
                    //请先退出日志列表页面
                    val notificationManager = NotificationManagerCompat.from(context)
                    val notification = createNotification(
                        context,
                        OPERATE_START,
                        context.getString(R.string.collect_logs_in_log_list_prompt),
                        R.drawable.ic_baseline_play_arrow_24,
                        context.getString(R.string.start_collect_logs)
                    )
                    notificationManager.notify(NOTIFICATION_ID, notification)
                    return
                }
                //开始收集日志
                val workManager = WorkManager.getInstance(context)
                val constraint = Constraints.Builder()
                    .build()
                val request = OneTimeWorkRequestBuilder<LogCatWorker>()
                    .setConstraints(constraint)
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .addTag(TAG_LOGCAT)
                    .build()
                workManager.enqueue(request)
                workManager.getWorkInfoByIdLiveData(request.id).observeForever {
                    if (it == null) {
                        return@observeForever
                    }
                    if (it.state == WorkInfo.State.RUNNING) {
                        //更新通知
                        val notificationManager = NotificationManagerCompat.from(context)
                        val notification = createNotification(
                            context,
                            OPERATE_STOP,
                            context.getString(R.string.collecting_logs),
                            R.drawable.ic_baseline_stop_24,
                            context.getString(R.string.stop_collect_logs)
                        )
                        notificationManager.notify(NOTIFICATION_ID, notification)
                    }
                }
            }
        }

        companion object {

        }
    }

    override fun onDestroy() {
        receiver?.let {
            unregisterReceiver(it)
        }
        super.onDestroy()
    }
}
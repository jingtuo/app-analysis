package io.github.jingtuo.android.aa.ui.model

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import io.github.jingtuo.android.aa.MyApp
import io.github.jingtuo.android.aa.R
import io.github.jingtuo.android.aa.repo.LogRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val logRepo = LogRepo(application)

    private val notificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    MyApp.CHANNEL_ID_LOG,
                    application.getString(R.string.channel_log),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }

    fun startLogCat() {
        logRepo.startLogCat()
    }

    override fun onCleared() {
        super.onCleared()
        logRepo.stopLogCat()
    }

    fun clearLog() {
        logRepo.clearLog()
    }

}
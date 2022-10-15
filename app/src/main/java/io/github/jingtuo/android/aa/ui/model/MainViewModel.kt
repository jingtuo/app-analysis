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

    override fun onCleared() {
        super.onCleared()
        logRepo.stopLogCat()
    }

    fun clearLog() {
        logRepo.clearLog()
    }

}
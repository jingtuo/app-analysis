package io.github.jingtuo.android.aa.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import io.github.jingtuo.android.aa.db.AaDatabase
import io.github.jingtuo.android.aa.db.LogDao
import io.github.jingtuo.android.aa.db.model.LogInfo
import io.github.jingtuo.android.aa.worker.ClearLogWorker
import io.github.jingtuo.android.aa.worker.LogCatWorker

const val WHITE_SPACE = ' '
const val COLON = ':'

class LogRepo(appContext: Context) {

    private val appContext: Context

    private val logDao: LogDao

    private val workManager: WorkManager

    init {
        this.appContext = appContext
        val database = Room.databaseBuilder(appContext, AaDatabase::class.java, AaDatabase.NAME)
            .build()
        logDao = database.logDao()
        workManager = WorkManager.getInstance(appContext)
    }

    fun getAll(): LiveData<List<LogInfo>> {
        return logDao.getAll()
    }

    fun stopLogCat() {
        workManager.cancelAllWorkByTag(TAG)
    }

    companion object {
        const val TAG = "LogCat"
    }

    fun clearLog() {
        val request = OneTimeWorkRequestBuilder<ClearLogWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        workManager.enqueue(request)
    }

    fun loadLogs(tag: String, priority: String, text: String): LiveData<List<LogInfo>> {
        return logDao.find(tag, priority, text)
    }
}
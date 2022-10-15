package io.github.jingtuo.android.aa.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.github.jingtuo.android.aa.repo.LogRepo
import io.github.jingtuo.android.aa.db.model.LogInfo
import java.text.SimpleDateFormat
import java.util.*

class LogViewModel(app: Application) : AndroidViewModel(app) {

    private val logRepo = LogRepo(app)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    private val logs = MediatorLiveData<List<LogInfo>>()

    private var source: LiveData<List<LogInfo>>? = null

    fun logs(): LiveData<List<LogInfo>> {
        return logs
    }

    fun format(time: Date): String {
        return dateFormat.format(time)
    }

    fun loadLogs(tag: String = "", priority: String = "All", text: String = "") {
        source?.let {
            logs.removeSource(it)
        }
        source = logRepo.loadLogs(tag, priority, text)
        source?.let {
            logs.addSource(it) { list ->
                logs.value = list
            }
        }
    }
}
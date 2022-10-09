package io.github.jingtuo.android.aa.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.jingtuo.android.aa.repo.LogRepo
import io.github.jingtuo.android.aa.db.model.LogInfo
import java.text.SimpleDateFormat
import java.util.*

class LogViewModel(app: Application) : AndroidViewModel(app) {

    private val logRepo = LogRepo(app)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    fun logs(): LiveData<List<LogInfo>> {
        return logRepo.getAll()
    }

    fun format(time: Date): String {
        return dateFormat.format(time)
    }
}
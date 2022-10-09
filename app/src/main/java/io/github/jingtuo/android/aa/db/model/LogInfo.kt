package io.github.jingtuo.android.aa.db.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import io.github.jingtuo.android.aa.repo.COLON
import io.github.jingtuo.android.aa.repo.WHITE_SPACE
import java.text.SimpleDateFormat
import java.util.Date

@Entity(tableName = "log_info")
data class LogInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val pid: Int, val tid: Int,
    var time: Date,
    val tag: String, val priority: String,
) {
    var content: String = ""
}

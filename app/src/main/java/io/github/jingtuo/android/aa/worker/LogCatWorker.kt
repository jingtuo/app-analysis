package io.github.jingtuo.android.aa.worker

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.work.*
import io.github.jingtuo.android.aa.db.AaDatabase
import io.github.jingtuo.android.aa.db.LogDao
import io.github.jingtuo.android.aa.db.model.LogInfo
import io.github.jingtuo.android.aa.repo.COLON
import io.github.jingtuo.android.aa.repo.WHITE_SPACE
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

const val CACHE_SIZE = 100

class LogCatWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private val logDao: LogDao

    private val logs = mutableListOf<LogInfo>()

    init {
        val database = Room.databaseBuilder(appContext, AaDatabase::class.java, "app_analysis")
            .build()
        logDao = database.logDao()
    }

    override fun doWork(): Result {
        collectLog()
        return Result.success()
    }

    private fun collectLog() {
        val dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.getDefault())
        val processBuilder = ProcessBuilder().command("logcat", "-v year,threadtime")
        var preLogInfo: LogInfo? = null
        var process: Process? = null
        var reader: BufferedReader? = null
        try {
            process = processBuilder.start()
            reader = BufferedReader(InputStreamReader(process.inputStream))
            while (true) {
                val line = reader.readLine()
                if (line != null) {
                    if (line.startsWith("-")) {
                        if (line.contains("beginning of main")
                            || line.contains("PROCESS STARTED")
                        ) {
                            //开始
                            continue
                        }
                    } else if (line.startsWith(" ")) {
                        preLogInfo?.let {
                            //追加内容
                            it.content += "\n" + line.trimStart()
                        }
                    } else {
                        val logInfo = parse(line, dataFormat)
                        logInfo?.let {
                            preLogInfo?.let {
                                logs.add(it)
                                if (logs.size >= CACHE_SIZE) {
                                    logDao.insertLogs(logs)
                                    logs.clear()
                                }
                            }
                            preLogInfo = logInfo
                        }
                    }
                } else {
                    preLogInfo?.let {
                        logs.add(it)
                        logDao.insertLogs(logs)
                        logs.clear()
                    }
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                process?.destroyForcibly()
            } else {
                process?.destroy()
            }
        }
    }

    private fun parse(line: String, dateFormat: SimpleDateFormat): LogInfo? {
        var startIndex = 0
        //日期 + 时间 + 时区 29字符
        var dateTime: Date? = null
        try {
            dateTime = dateFormat.parse(line.substring(startIndex, 29))
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        startIndex = 30
        //pid
        val pid = line.substring(startIndex, startIndex + 5).trim().toInt()
        //tid
        startIndex += 6
        val tid = line.substring(startIndex, startIndex + 5).trim().toInt()
        startIndex += 6
        var index = line.indexOf(WHITE_SPACE, startIndex)
        //priority
        var priority = line.substring(startIndex, index)
        priority = when (priority) {
            "D" -> "Debug"
            "I" -> "Info"
            "W" -> "Warn"
            "E" -> "Error"
            "F" -> "Fatal"
            "S" -> "Silent"
            else -> "Verbose"
        }
        startIndex = index + 1
        //tag
        index = line.indexOf(COLON, startIndex)
        val tag = line.substring(startIndex, index)
        //content
        val content = line.substring(index + 2)
        val logInfo = LogInfo(
            null,
            pid, tid,
            dateTime!!, tag, priority
        )
        logInfo.content = content
        return logInfo
    }

    override fun onStopped() {
        if (logs.isNotEmpty()) {
            logDao.insertLogs(logs)
            logs.clear()
        }
        super.onStopped()
    }
}
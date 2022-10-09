package io.github.jingtuo.android.aa.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.jingtuo.android.aa.db.model.LogInfo

@Dao
interface LogDao {
    @Query("select * from log_info order by time desc")
    fun getAll(): LiveData<List<LogInfo>>

    @Query("select * from log_info where content like :text")
    fun find(text: String): LiveData<List<LogInfo>>

    @Insert
    fun insertLogs(logs: List<LogInfo>): Array<Long>
}
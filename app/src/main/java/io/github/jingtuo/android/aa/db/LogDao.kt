package io.github.jingtuo.android.aa.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.jingtuo.android.aa.db.model.LogInfo
import java.util.*

/**
 * 插入数据返回的是rowId: Long
 * 删除数据返回的是删除行数: Int
 */
@Dao
interface LogDao {
    @Query("select * from log_info order by time desc")
    fun getAll(): LiveData<List<LogInfo>>

    @Query("select * from log_info where content like :text")
    fun find(text: String): LiveData<List<LogInfo>>

    @Insert
    fun insertLogs(logs: List<LogInfo>): Array<Long>

    @Query("delete from log_info where time <= :time")
    fun deleteBefore(time: Date): Int
}
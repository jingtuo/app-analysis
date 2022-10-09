package io.github.jingtuo.android.aa.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.jingtuo.android.aa.repo.Converters
import io.github.jingtuo.android.aa.db.model.LogInfo

@Database(
    entities = [LogInfo::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AaDatabase: RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {
        const val NAME = "app_analysis"
    }
}
package io.github.jingtuo.android.aa

import android.app.Application
import kotlin.properties.Delegates

class MyApp: Application() {

    //标记是否日志列表
    var inLogList: Boolean = false

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        const val CHANNEL_ID_LOG = "Log"
        const val NOTIFICATION_ID = "notification_id"
        private var instance: MyApp by Delegates.notNull()
        fun instance() = instance
    }
}
package io.github.jingtuo.android.aa

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        //严苛模式
//        StrictMode.setThreadPolicy(ThreadPolicy.Builder()
//            .detectAll()
//            .penaltyLog()
//            .build())
    }

    companion object {
        const val CHANNEL_ID_LOG = "Log"
    }
}
package io.github.jingtuo.android.aa.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

    }

    companion object {
        const val ACTION_LOGCAT = "io.github.jingtuo.android.aa.LOGCAT"
    }
}
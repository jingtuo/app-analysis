package io.github.jingtuo.android.aa.ext

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager

fun ActivityInfo.label(packageManager: PackageManager): String {
    val activityName = loadLabel(packageManager).toString()
    val appName = applicationInfo.loadLabel(packageManager).toString()
    if (appName == activityName) {
        val clsName = clsName()
        val index = clsName.lastIndexOf(".")
        if (index >= 0) {
            return clsName.substring(index + 1)
        }
        return clsName
    }
    return activityName
}

fun ActivityInfo.clsName(): String {
    return if (name.startsWith(".")) {
        packageName + name
    } else {
        name
    }
}
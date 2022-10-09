package io.github.jingtuo.android.aa.ext

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager

fun ActivityInfo.label(packageManager: PackageManager): String {
    return loadLabel(packageManager).toString()
}

fun ActivityInfo.clsName(): String {
    return if (name.startsWith(".")) {
        packageName + name
    } else {
        name
    }
}
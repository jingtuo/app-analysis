package io.github.jingtuo.android.aa.ext

import android.content.pm.PackageInfo
import android.content.pm.PackageManager

fun PackageInfo.label(packageManager: PackageManager): String {
    return applicationInfo.loadLabel(packageManager).toString()
}
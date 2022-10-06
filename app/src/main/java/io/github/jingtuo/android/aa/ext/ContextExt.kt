package io.github.jingtuo.android.aa.ext

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import io.github.jingtuo.android.aa.ui.model.ActivityInfo
import io.github.jingtuo.android.aa.ui.model.PkgInfo

//跟包名获取包
fun Context.getPkg(pkgName: String): PkgInfo {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(pkgName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        packageManager.getPackageInfo(pkgName, 0)
    }
    val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
    val icon = packageInfo.applicationInfo.loadIcon(packageManager)
    return PkgInfo(pkgName, appName, icon)
}

//获取所有包
fun Context.getAllPkg(): List<PkgInfo> {
    val start = System.currentTimeMillis()
    val packageList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getInstalledPackages(0)
    }
    val result = mutableListOf<PkgInfo>()
    for (item in packageList) {
        val pkgName = item.packageName
        val appName = item.applicationInfo.loadLabel(packageManager).toString()
        val icon = item.applicationInfo.loadIcon(packageManager)
        result.add(PkgInfo(pkgName, appName, icon))
    }
    return result.toList()
}

//获取页面
fun Context.getAllActivity(pkgName: String): List<ActivityInfo> {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(pkgName,
            PackageManager.PackageInfoFlags.of(PackageManager.GET_ACTIVITIES.toLong())
        )
    } else {
        packageManager.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES)
    }
    val result = mutableListOf<ActivityInfo>()
    val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
    if (packageInfo.activities != null) {
        for (item in packageInfo.activities) {
            var name = item.loadLabel(packageManager).toString()
            val clsName = if (item.name.startsWith(".")) {
                item.packageName + item.name
            } else {
                item.name
            }
            if (name == appName) {
                name = item.name.substring(item.name.lastIndexOf('.') + 1)
            }
            result.add(ActivityInfo(name, clsName))
        }
    }
    return result.toList()
}
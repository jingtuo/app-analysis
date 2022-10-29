package io.github.jingtuo.android.aa.ext

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import io.github.jingtuo.android.aa.ui.model.Permission

//跟包名获取包
fun Context.packageInfo(pkgName: String): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(
            pkgName, PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        packageManager.getPackageInfo(pkgName, 0)
    }
}

//获取所有包
fun Context.packages(): List<PackageInfo> {
    var flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        PackageManager.MATCH_UNINSTALLED_PACKAGES
    } else {
        PackageManager.GET_UNINSTALLED_PACKAGES
    }
    flags = flags or PackageManager.GET_SIGNATURES
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        flags = flags or PackageManager.GET_SIGNING_CERTIFICATES
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        packageManager.getInstalledPackages(flags)
    }
}

//获取页面
fun Context.activities(pkgName: String): List<ActivityInfo> {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(
            pkgName, PackageManager.PackageInfoFlags.of(PackageManager.GET_ACTIVITIES.toLong())
        )
    } else {
        packageManager.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES)
    }
    if (packageInfo.activities == null) {
        return emptyList()
    }
    return packageInfo.activities.toList()
}

//获取权限
fun Context.permissions(pkgName: String): List<Permission> {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(
            pkgName, PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
        )
    } else {
        packageManager.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS)
    }
    val result = mutableListOf<Permission>()
    //请求权限
    val sysPermissions = packageInfo.requestedPermissions.toSet()
    for (item in sysPermissions) {
        var permissionInfo: PermissionInfo? = null
        try {
            permissionInfo = packageManager.getPermissionInfo(item, PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (permissionInfo == null) {
            val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PermissionInfo.PROTECTION_INTERNAL
            } else {
                PermissionInfo.PROTECTION_SIGNATURE
            }
            result.add(Permission(code = item, type = type))
        } else {
            val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                permissionInfo.protection
            } else {
                permissionInfo.protectionLevel
            }
            result.add(Permission(code = permissionInfo.name, type = type))
        }
    }
    //自定义权限
    if (packageInfo.permissions != null) {
        for (item in packageInfo.permissions) {
            val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                item.protection
            } else {
                item.protectionLevel
            }
            result.add(Permission(code = item.name, type = type))
        }
    }
    return result;
}
package io.github.jingtuo.android.aa.ui.model

import android.content.pm.PermissionInfo

data class Permission(val code: String, val type: Int) {

    fun name(): String {
        val androidPermissionPrefix = "android.permission."
        return if (code.startsWith(androidPermissionPrefix)) {
            //系统权限
            code.substring(androidPermissionPrefix.length)
        } else {
            //自定义权限
            code
        }
    }

    fun typeName(): String {
        return when(type) {
            PermissionInfo.PROTECTION_NORMAL -> "普通权限"
            PermissionInfo.PROTECTION_DANGEROUS -> "危险权限"
            PermissionInfo.PROTECTION_SIGNATURE -> "签名权限"
            PermissionInfo.PROTECTION_INTERNAL -> "内部权限"
            else -> "其他权限/自定义权限"
        }
    }

}

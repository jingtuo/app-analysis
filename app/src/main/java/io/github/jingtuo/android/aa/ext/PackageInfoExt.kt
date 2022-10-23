package io.github.jingtuo.android.aa.ext

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

fun PackageInfo.label(packageManager: PackageManager): String {
    return applicationInfo.loadLabel(packageManager).toString()
}

fun PackageInfo.signInfo(algorithm: String, useColon: Boolean = false): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val signer = signingInfo.signingCertificateHistory[0]
        signer.toString(algorithm, useColon)
    } else {
        val signer = signatures[0]
        signer.toString(algorithm, useColon)
    }
}
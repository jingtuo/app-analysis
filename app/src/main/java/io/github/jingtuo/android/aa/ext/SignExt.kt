package io.github.jingtuo.android.aa.ext

import android.content.pm.Signature
import java.security.MessageDigest

fun Signature.toString(algorithm: String, useColon: Boolean = false): String {
    val encrypt = MessageDigest.getInstance(algorithm)
    val array = encrypt.digest(toByteArray())
    var result = ""
    var temp = ""
    for ((index, item) in array.withIndex()) {
        temp = (item.toInt() and 0xFF).toString(16)
        if (temp.length <= 1) {
            result += "0"
        }
        result += temp
        if (useColon) {
            if (index != array.size - 1) {
                result += ":"
            }
        }
    }
    return result
}
package com.example.simplehero.utils

import java.math.BigInteger
import java.security.MessageDigest

class UtilsFun {
    companion object {

        fun md5(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }

        fun httpToHttps(url: String): String {
            return url.replace("http", "https")
        }
    }
}

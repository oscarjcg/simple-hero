package com.example.simplehero.utils

class ApiKeyGenerator {
    var ts: String = ""
    var hash: String = ""

    fun generate() {
        ts = System.currentTimeMillis().toString()
        hash = UtilsFun.md5(ts + PRIVATE_APIKEY + APIKEY)
    }
}

package com.example.simplehero.utils

/**
 * Generates api values for webservice.
 */
class ApiKeyGenerator() {
    var ts: String = ""
    var hash: String = ""

    init {
        generate()
    }

    fun generate() {
        ts = System.currentTimeMillis().toString()
        hash = UtilsFun.md5(ts + PRIVATE_APIKEY + APIKEY)
    }
}

package com.arkansasio.rpgmmo.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class MmoApiClient(private val baseUrl:String) {
    suspend fun get(path:String):Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val connection=URL(baseUrl.trimEnd('/')+"/"+path.trimStart('/')).openConnection() as HttpURLConnection
            connection.requestMethod="GET"; connection.connectTimeout=5000; connection.readTimeout=5000
            connection.inputStream.bufferedReader().use{it.readText()}.also{connection.disconnect()}
        }
    }
}

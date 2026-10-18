package com.arkansasio.rpgmmo.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

@Serializable data class LoginRequest(val username:String,val password:String)
@Serializable data class LoginResponse(val accountId:String,val displayName:String,val accessToken:String,val expiresAtEpochMs:Long)

class AuthApi(private val baseUrl:String){
    suspend fun login(username:String,password:String):Result<LoginResponse> = postJson("auth/login",LoginRequest(username,password))
    suspend fun register(username:String,password:String):Result<LoginResponse> = postJson("auth/register",LoginRequest(username,password))
    private suspend inline fun <reified T> postJson(path:String,body:T):Result<LoginResponse> = withContext(Dispatchers.IO){runCatching{
        val c=URL(baseUrl.trimEnd('/')+"/"+path).openConnection() as HttpURLConnection
        c.requestMethod="POST";c.doOutput=true;c.connectTimeout=7000;c.readTimeout=7000;c.setRequestProperty("Content-Type","application/json")
        c.outputStream.use{it.write(Json.encodeToString(body).toByteArray())}
        val response=c.inputStream.bufferedReader().use{it.readText()};c.disconnect();Json.decodeFromString<LoginResponse>(response)
    }}
}

package com.arkansasio.rpgmmo.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MmoApiClient(private val baseUrl:String, private var accessToken:String?=null) {
    private val json=Json{ignoreUnknownKeys=true}
    fun setAccessToken(token:String?){accessToken=token}
    suspend fun get(path:String):Result<String> = request("GET",path,null)
    suspend fun post(path:String,body:String):Result<String> = request("POST",path,body)
    suspend inline fun <reified T> postJson(path:String,body:T):Result<String> = post(path,json.encodeToString(body))
    suspend fun login(request:LoginRequest):Result<SessionResponse> = postJson(ServerEndpoints.LOGIN,request).mapCatching{json.decodeFromString<SessionResponse>(it)}
    suspend fun register(request:RegisterRequest):Result<SessionResponse> = postJson(ServerEndpoints.REGISTER,request).mapCatching{json.decodeFromString<SessionResponse>(it)}
    suspend fun refresh(request:RefreshRequest):Result<SessionResponse> = postJson(ServerEndpoints.REFRESH,request).mapCatching{json.decodeFromString<SessionResponse>(it)}
    suspend fun createCharacter(request:CharacterCreateRequest):Result<CharacterResponse> = postJson(ServerEndpoints.CHARACTER,request).mapCatching{json.decodeFromString<CharacterResponse>(it)}
    suspend fun sync(request:SyncRequest):Result<SyncResponse> = postJson(ServerEndpoints.SYNC,request).mapCatching{json.decodeFromString<SyncResponse>(it)}
    suspend fun move(request:MoveRequest):Result<CharacterResponse> = postJson(ServerEndpoints.MOVE,request).mapCatching{json.decodeFromString<CharacterResponse>(it)}
    suspend fun combat(request:CombatRequest):Result<CombatResponse> = postJson(ServerEndpoints.COMBAT,request).mapCatching{json.decodeFromString<CombatResponse>(it)}
    suspend fun guild(request:GuildRequest):Result<String> = postJson(ServerEndpoints.GUILD,request)
    suspend fun party(request:PartyRequest):Result<String> = postJson(ServerEndpoints.PARTY,request)
    suspend fun queueMatch(request:MatchmakingRequest):Result<MatchmakingResponse> = postJson(ServerEndpoints.MATCHMAKING,request).mapCatching{json.decodeFromString<MatchmakingResponse>(it)}

    private suspend fun request(method:String,path:String,body:String?):Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            require(baseUrl.startsWith("https://")){"MMO API requires HTTPS"}
            val connection=URL(baseUrl.trimEnd('/')+"/"+path.trimStart('/')).openConnection() as HttpURLConnection
            try {
                connection.requestMethod=method
                connection.connectTimeout=8000
                connection.readTimeout=10000
                connection.setRequestProperty("Accept","application/json")
                accessToken?.let{connection.setRequestProperty("Authorization","Bearer $it")}
                if(body!=null){connection.doOutput=true;connection.setRequestProperty("Content-Type","application/json");connection.outputStream.use{it.write(body.toByteArray(Charsets.UTF_8))}}
                val code=connection.responseCode
                val stream=if(code in 200..299) connection.inputStream else connection.errorStream
                val text=stream?.bufferedReader()?.use{it.readText()} ?: ""
                if(code !in 200..299) error("HTTP $code: $text")
                text
            } finally { connection.disconnect() }
        }
    }
}

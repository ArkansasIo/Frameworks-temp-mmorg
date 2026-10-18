package com.arkansasio.rpgmmo.server

import io.ktor.server.application.Application
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

object RealtimeHub {
    private val sessions=ConcurrentHashMap<String,MutableSet<DefaultWebSocketServerSession>>()
    private val json=Json{encodeDefaults=true}

    fun install(app:Application){
        app.install(WebSockets)
        app.routing {
            webSocket("/v1/realtime") {
                val token=call.request.headers["Authorization"]?.removePrefix("Bearer ")?.trim()
                val config=ServerConfig.fromEnvironment()
                val user=token?.let{Tokens.verifyAccess(it,config)}
                if(user==null){close(CloseReason(CloseReason.Codes.VIOLATED_POLICY,"Unauthorized"));return@webSocket}
                val bucket=sessions.computeIfAbsent(user){ConcurrentHashMap.newKeySet()};bucket.add(this)
                try { for(frame in incoming){ if(frame is Frame.Text && frame.readText()=="ping") send(Frame.Text("pong")) } }
                finally { bucket.remove(this);if(bucket.isEmpty())sessions.remove(user) }
            }
        }
    }

    suspend fun broadcast(userId:String,event:String){sessions[userId]?.toList()?.forEach{runCatching{it.send(Frame.Text(json.encodeToString(ServerEvent(event,System.currentTimeMillis()))))}}}
}

@Serializable data class ServerEvent(val type:String,val serverTimestampEpochMs:Long)

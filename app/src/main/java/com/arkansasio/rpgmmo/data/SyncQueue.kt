package com.arkansasio.rpgmmo.data

import android.content.Context
import com.arkansasio.rpgmmo.network.SyncOperation
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable private data class QueueEnvelope(val operations:List<SyncOperation>=emptyList())

class SyncQueue(context:Context) {
    private val prefs=context.getSharedPreferences("rpg_mmo_sync",Context.MODE_PRIVATE)
    private val json=Json{ignoreUnknownKeys=true}
    @Synchronized fun enqueue(operation:SyncOperation){
        val next=(load()+operation).distinctBy{it.id}
        prefs.edit().putString("queue",json.encodeToString(QueueEnvelope(next))).apply()
    }
    @Synchronized fun peek():List<SyncOperation> = prefs.getString("queue",null)?.let{runCatching{json.decodeFromString<QueueEnvelope>(it).operations}.getOrDefault(emptyList())} ?: emptyList()
    @Synchronized fun remove(ids:Set<String>){
        val next=peek().filterNot{it.id in ids}
        prefs.edit().putString("queue",json.encodeToString(QueueEnvelope(next))).apply()
    }
    fun size():Int=peek().size
    fun clear(){prefs.edit().remove("queue").apply()}
    private fun load():List<SyncOperation>=peek()
}

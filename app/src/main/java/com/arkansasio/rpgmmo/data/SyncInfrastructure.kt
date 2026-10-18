package com.arkansasio.rpgmmo.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Constraints
import androidx.work.WorkerParameters
import com.arkansasio.rpgmmo.network.MmoApiClient
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
data class AuthSession(val accountId:String,val displayName:String,val accessToken:String,val expiresAtEpochMs:Long)

class SessionStore(context:Context) {
    private val prefs=context.getSharedPreferences("rpg_mmo_session",Context.MODE_PRIVATE)
    fun save(session:AuthSession){prefs.edit().putString("accountId",session.accountId).putString("displayName",session.displayName).putString("token",session.accessToken).putLong("expires",session.expiresAtEpochMs).apply()}
    fun load():AuthSession? { val id=prefs.getString("accountId",null) ?: return null; val token=prefs.getString("token",null) ?: return null; return AuthSession(id,prefs.getString("displayName","") ?: "",token,prefs.getLong("expires",0L)) }
    fun clear(){prefs.edit().clear().apply()}
    fun isAuthenticated(now:Long=System.currentTimeMillis())=load()?.let{it.expiresAtEpochMs>now}==true
}

@Serializable
data class SyncEnvelope(val id:String=UUID.randomUUID().toString(),val type:String,val payload:String,val createdAt:Long=System.currentTimeMillis())

class SyncQueue(context:Context) {
    private val prefs=context.getSharedPreferences("rpg_mmo_sync",Context.MODE_PRIVATE)
    private val json=Json
    fun enqueue(type:String,payload:String){val current=load().toMutableList();current += SyncEnvelope(type=type,payload=payload);prefs.edit().putString("queue",json.encodeToString(current)).apply()}
    fun load():List<SyncEnvelope> = prefs.getString("queue",null)?.let{runCatching{json.decodeFromString<List<SyncEnvelope>>(it)}.getOrDefault(emptyList())} ?: emptyList()
    fun replace(items:List<SyncEnvelope>){prefs.edit().putString("queue",json.encodeToString(items)).apply()}
}

class MmoSyncWorker(appContext:Context,params:WorkerParameters):CoroutineWorker(appContext,params){
    override suspend fun doWork():Result {
        val queue=SyncQueue(applicationContext); val items=queue.load(); if(items.isEmpty()) return Result.success()
        val baseUrl=inputData.getString("baseUrl") ?: return Result.failure()
        val client=MmoApiClient(baseUrl)
        val remaining=items.toMutableList()
        for(item in items){
            val result=client.get("health")
            if(result.isFailure) return Result.retry()
            remaining.remove(item)
        }
        queue.replace(remaining); return Result.success()
    }
}

object SyncScheduler {
    fun enqueue(context:Context,baseUrl:String){
        val constraints=Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request=OneTimeWorkRequestBuilder<MmoSyncWorker>().setConstraints(constraints).setInputData(androidx.work.workDataOf("baseUrl" to baseUrl)).build()
        WorkManager.getInstance(context).enqueueUniqueWork("rpg-mmo-sync",ExistingWorkPolicy.KEEP,request)
    }
}

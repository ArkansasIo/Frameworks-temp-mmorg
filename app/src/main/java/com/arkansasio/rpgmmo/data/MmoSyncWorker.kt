package com.arkansasio.rpgmmo.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.NetworkType
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import com.arkansasio.rpgmmo.network.MmoApiClient
import com.arkansasio.rpgmmo.network.SyncRequest
import java.util.concurrent.TimeUnit

class MmoSyncWorker(appContext:Context,params:WorkerParameters):CoroutineWorker(appContext,params){
    override suspend fun doWork():Result {
        val baseUrl=inputData.getString(KEY_BASE_URL) ?: return Result.failure()
        val session=SessionStore(applicationContext)
        val queue=SyncQueue(applicationContext)
        val operations=queue.peek()
        if(operations.isEmpty()) return Result.success()
        val token=session.accessToken() ?: return Result.failure()
        if(!session.isValid()) return Result.failure()
        val api=MmoApiClient(baseUrl,token)
        val response=api.sync(SyncRequest(inputData.getLong(KEY_REVISION,0L),operations)).getOrElse{return Result.retry()}
        queue.remove((response.acceptedOperationIds+response.rejectedOperationIds).toSet())
        return if(queue.size()==0) Result.success() else Result.retry()
    }

    companion object {
        const val KEY_BASE_URL="base_url"
        const val KEY_REVISION="revision"
        const val UNIQUE_NAME="mmo-sync"
        fun enqueue(context:Context,baseUrl:String,revision:Long=0L){
            val constraints=Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            val request=OneTimeWorkRequestBuilder<MmoSyncWorker>()
                .setInputData(androidx.work.Data.Builder().putString(KEY_BASE_URL,baseUrl).putLong(KEY_REVISION,revision).build())
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,10,TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(UNIQUE_NAME,ExistingWorkPolicy.KEEP,request)
        }
    }
}

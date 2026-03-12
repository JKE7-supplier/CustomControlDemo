/* (c) Disney. All rights reserved. */
package com.example.demo.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by kec005  on 3/12/26.
 */
class MyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    companion object {
        const val TAG: String = "ChainWorker"
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val workerRequestName = inputData.getString(TAG)
                println("worker------------->worker name: $workerRequestName")
                Thread.sleep(3000)
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }
        }
    }
}
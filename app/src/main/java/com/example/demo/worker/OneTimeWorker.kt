/* (c) Disney. All rights reserved. */
package com.example.demo.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by kec005  on 3/12/26.
 */
class OneTimeWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object{
        const val WORKER_NAME_KEY = "worker_name"
    }

    override fun doWork(): Result {
        return try {
            println("worker--------开始执行具体任务--------->worker name: ${inputData.getString(WORKER_NAME_KEY)}")
            Thread.sleep(5000)
            println("worker--------具体任务完成------------>")
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
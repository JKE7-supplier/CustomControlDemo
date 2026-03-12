/* (c) Disney. All rights reserved. */
package com.example.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.demo.databinding.WorkerManagerLayoutBinding
import com.example.demo.worker.MockWorker
import com.example.demo.worker.MyWorker
import com.example.demo.worker.OneTimeWorker
import java.util.concurrent.TimeUnit

/**
 * Created by kec005  on 3/10/26.
 */
class WorkerManagerActivity : AppCompatActivity() {
    private lateinit var bindingView: WorkerManagerLayoutBinding
    private val workManager: WorkManager by lazy { WorkManager.getInstance(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = WorkerManagerLayoutBinding.inflate(layoutInflater)
        setContentView(bindingView.root)
        initView()
    }

    private fun initView() {
        bindingView.requestWorkerABtn.setOnClickListener {
            startOneTimeWorker()
        }

        bindingView.bBtn.setOnClickListener {
            startPeriodicWorker()
        }

        bindingView.cBtn.setOnClickListener {
            startChainWorker()
        }
    }

    private fun startOneTimeWorker() {
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<OneTimeWorker>()
            .setConstraints(buildWorkerConstraints())
            .setInputData(workDataOf(OneTimeWorker.WORKER_NAME_KEY to "一次性工作"))//支持基本数据类型
            .build()

        workManager.beginUniqueWork(
            OneTimeWorker.WORKER_NAME_KEY,
            ExistingWorkPolicy.KEEP,
            oneTimeWorkRequest
        ).enqueue()

    }

    private fun startPeriodicWorker() {
        val periodicWorkerRequest = PeriodicWorkRequestBuilder<MockWorker>(repeatInterval = 15, repeatIntervalTimeUnit = TimeUnit.MINUTES)
            .setInputData(workDataOf(MockWorker.WORKER_NAME_KEY to "周期性工作"))
            .setConstraints(buildWorkerConstraints())
            .setBackoffCriteria(
                BackoffPolicy.LINEAR, // 线性退避
                10,// 初始延迟
                TimeUnit.SECONDS
            )//retry策略 ，10s → 20s → 30s → 40s ...
            .setInitialDelay(3, TimeUnit.SECONDS)//首次执行延迟 3 秒
            .build()


        workManager.enqueueUniquePeriodicWork(
            MockWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,//冲突策略,如果已有同名任务，保留旧的，忽略新的
            periodicWorkerRequest
        )

        workManager.getWorkInfoByIdLiveData(periodicWorkerRequest.id).observe(this) { workInfo ->
            println("worker----------周期性-------任务执行状态state: ${workInfo?.state} outputData：${workInfo?.outputData}")
        }
    }

    private fun startChainWorker() {
        val workerRequest1 = OneTimeWorkRequestBuilder<MyWorker>().setInputData(workDataOf(MyWorker.TAG to "workerRequest1")).build()
        val workerRequest2 = OneTimeWorkRequestBuilder<MyWorker>().setInputData(workDataOf(MyWorker.TAG to "workerRequest2")).build()
        val workerRequest3 = OneTimeWorkRequestBuilder<MyWorker>().setInputData(workDataOf(MyWorker.TAG to "workerRequest3")).build()

        workManager.beginUniqueWork(
            "chain_worker_tag",
            ExistingWorkPolicy.KEEP,
            workerRequest1
        ).then(workerRequest2)
            .then(workerRequest3)
            .enqueue()
    }

    private fun buildWorkerConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)// 不需要网络
            .setRequiresBatteryNotLow(false)  // 不要求电量充足
            .setRequiresCharging(false)  // 不要求充电
            .setRequiresDeviceIdle(false)  // 不要求设备空闲
            .setRequiresStorageNotLow(false)  // 不要求存储充足
            .build()
    }
}
/* (c) Disney. All rights reserved. */
package com.example.demo.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.random.Random

/**
 * Created by kec005  on 3/11/26.
 */
class MockWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val TAG = "MockWorker"
        const val WORKER_NAME_KEY = "worker_name"
        const val MAX_RETRIES = 5
    }

    override suspend fun doWork(): Result {
        println("worker------MockWorker-----------worker name : ${inputData.getString(WORKER_NAME_KEY)}")
        return withContext(Dispatchers.IO) {
            try {
                //执行耗时任务、比如请求接口、数据库读写、图片处理等。此处延迟一会儿来模拟耗时任务。
                val response = mockNetworkRequest()
                if (response.isSuccessful) {
                    Result.success(workDataOf("message" to "请求成功"))
                } else {
                    handleServerError(response.code)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                // 网络异常、超时等
                println("worker-----------------exception: ${exception.message}")
                handleNetworkError(exception)
            }
        }
    }

    // 模拟网络请求
    private suspend fun mockNetworkRequest(): WorkerResponse {
        // 模拟网络请求耗时
        delay(2000)
        // 模拟不同响应
        return when (Random.nextInt(10)) {// 生成一个 0-9 的随机数
            0, 1, 2 -> WorkerResponse(200, true)  // 成功
            3, 4 -> WorkerResponse(500, false)    // 服务端错误
            5, 6 -> WorkerResponse(400, false)    // 客户端错误
            else -> throw IOException("模拟网络异常")
        }
    }

    private fun handleServerError(code: Int): Result {
        return when (code) {
            in 400..499 -> {
                // 客户端错误，不重试
                println("worker-----------------客户端错误: $code 不重试")
                Result.failure(
                    workDataOf("error" to "客户端错误: $code")
                )
            }

            in 500..599 -> {
                // 服务端错误，重试; runAttemptCount 获取此任务的当前运行尝试次数。
                if (runAttemptCount < MAX_RETRIES) {
                    println("worker-----------------服务端错误，准备重试: $code  当前重试次数: $runAttemptCount")
                    Result.retry()
                } else {
                    println("worker-----------------服务端错误，已达最大重试次数")
                    Result.failure(
                        workDataOf("error" to "服务端错误: $code")
                    )
                }
            }

            else -> {
                // 其他状态码
                println("worker-----------------其他状错误")
                Result.failure(
                    workDataOf("error" to "未知错误: $code")
                )
            }
        }
    }

    private fun handleNetworkError(exception: Exception): Result {
        return when (exception) {
            is SocketTimeoutException,
            is ConnectException,
            is UnknownHostException -> {
                // 网络相关异常，重试
                if (runAttemptCount < MAX_RETRIES) {
                    println("worker-----------------网络异常，准备重试: ${exception.message}")
                    Result.retry()
                } else {
                    println("worker-----------------网络异常，已达最大重试次数")
                    Result.failure(
                        workDataOf("error" to "网络异常: ${exception.message}")
                    )
                }
            }

            else -> {
                // 其他异常，不重试
                println("base-----------------未知异常: ${exception.message}")
                Result.failure(
                    workDataOf("error" to "未知异常: ${exception.message}")
                )
            }
        }
    }

}
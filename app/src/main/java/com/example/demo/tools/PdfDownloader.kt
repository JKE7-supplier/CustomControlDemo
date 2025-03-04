package com.example.demo.tools

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.BufferedSource
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException

/**
 * Created by kec005  on 8/15/24.
 */
object PdfDownloader {

    private val client = OkHttpClient()

    fun downloadPdf(url: String, destinationFile: File, onComplete: (File) -> Unit, onFailure: (Exception) -> Unit) {
        if (destinationFile.exists()) {
            destinationFile.delete()
        }

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("base------pdf 下载失败 onFailure------>")
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                println("base------pdf 下载成功 onResponse------>")
                response.body?.let {
                    try {
                        val file = savePdfToFile(it.source(), destinationFile, onFailure)
                        file?.let { pdfFile -> onComplete(pdfFile) }
                    } catch (e: IOException) {
                        onFailure(e)
                    }
                }
            }
        })
    }

    fun deleteFile(file: File): Boolean {
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    private fun savePdfToFile(source: BufferedSource, destinationFile: File, onFailure: (Exception) -> Unit): File? {
        return try {
            destinationFile.sink().buffer().use { sink ->
                sink.writeAll(source)
            }
            println("base------pdf 保存成功------>")
            destinationFile
        } catch (e: IOException) {
            println("base------pdf 保存失败------>")
            onFailure(e)
            null
        }
    }
}
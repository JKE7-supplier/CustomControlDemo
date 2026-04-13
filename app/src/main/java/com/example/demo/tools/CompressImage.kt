package com.example.demo.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.sqrt

/**
 * Created by kec005  on 4/13/26.
 */
object CompressImage {
    private const val shareFileName = "share_image.jpg"
    private const val compressedShareImage: String = "compressed_share_image"
    private const val fileChild = "share_images"
    private const val imageTypeJpg: String = ".jpg"


    fun compressImageInBackground(context: Context, originalBitmap: Bitmap, sizeInKB: Int, callback: CompressionCallback) {
        val compressionThread = Thread {
            val originalBitmapFile = formatBitmapToFile(context, originalBitmap)
            if (checkCompressImageFile(originalBitmapFile, sizeInKB)) {
                val imageFile = handleCompressBitmapToFile(originalBitmap, sizeInKB)
                if (imageFile == null) {
                    callback.onCompressionError()
                } else {
                    callback.onCompressionComplete(imageFile)
                }
            } else {
                callback.onCompressionComplete(originalBitmapFile)
            }
        }
        compressionThread.start()
    }


    private fun checkCompressImageFile(imageFile: File, maxSizeInKB: Int): Boolean {
        val imageFileSizeKB: Double = imageFile.length() / 1024.0
        return imageFileSizeKB > maxSizeInKB
    }

    private fun formatBitmapToFile(context: Context, bitmap: Bitmap): File {
        val directory = buildShareBitmapFileDirectory(context)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, System.currentTimeMillis().toString() + "_" + shareFileName)
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        return file
    }

    private fun buildShareBitmapFileDirectory(context: Context): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileChild)
    }

    private fun handleCompressBitmapToFile(bitmap: Bitmap, targetSizeKB: Int): File? {
        val outputStream = ByteArrayOutputStream()
        val quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        var low = 0
        var high = 100
        var bestQuality = 0
        var whileCount = 0

        while (low <= high && whileCount < 6) {
            whileCount += 1
            val mid = (low + high) / 2

            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, mid, outputStream)

            if (outputStream.size() <= targetSizeKB * 1024) {
                bestQuality = mid
                low = mid + 5
            } else {
                high = mid - 5
            }
        }

        if (outputStream.size() <= targetSizeKB * 1024) {
            //Create a temporary file, which will be automatically deleted when the JVM exits (for example, when the application is closed).
            val file = File.createTempFile(compressedShareImage, imageTypeJpg)
            val fileOutputStream = FileOutputStream(file)
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, bestQuality, fileOutputStream)
                outputStream.close()
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return file
        } else {
            //Crop and compress pictures
            outputStream.reset()
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, bestQuality, outputStream)
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val compressedByteArray = outputStream.toByteArray()
            val decodeBitmap = BitmapFactory.decodeByteArray(compressedByteArray, 0, compressedByteArray.size)
            return zoomBitmap(decodeBitmap, targetSizeKB)
        }

    }

    private fun zoomBitmap(srcBitmap: Bitmap, targetSizeKB: Int): File? {
        var bitmap = srcBitmap
        val targetSizeByte = targetSizeKB * 1024

        do {
            val bitmapStreamOutPut = ByteArrayOutputStream().apply {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            }

            if (bitmapStreamOutPut.size() <= targetSizeByte) {
                val bitmapFile = File.createTempFile(compressedShareImage, imageTypeJpg)
                FileOutputStream(bitmapFile).use { fileOutputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                }

                return bitmapFile
            }

            val aspectRatio = sqrt(targetSizeByte.toFloat() / bitmapStreamOutPut.size().toFloat())
            val matrix = Matrix().apply {
                postScale(aspectRatio, aspectRatio)
            }

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        } while (true)
    }

    interface CompressionCallback {
        fun onCompressionComplete(imageFile: File)
        fun onCompressionError()
    }
}
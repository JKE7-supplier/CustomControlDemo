/* (c) Disney. All rights reserved. */
package com.example.demo.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.demo.R
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by kec005  on 8/5/24.
 */
class QQShareUtils private constructor() {

    companion object {
        val instance: QQShareUtils by lazy { QQShareUtils() }
    }

    private var tencent: Tencent? = null

    fun initTencentQQSdk(applicationContext: Context) {
        Tencent.setIsPermissionGranted(true)
        tencent = Tencent.createInstance("102239733", applicationContext, "com.example.video_image_banner.fileprovider")
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, listener: IUiListener) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener)
    }

    fun shareLinkUrlToQQ(activity: Activity, listener: IUiListener, linkUrl: String) {
        if (tencent == null) return
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "标题") // 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要") // 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, linkUrl) // 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "https://www.xuebiaogan.com/uploads/allimg/210610/1-210610101235105.jpg") // 网络图片地址
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name)) // 应用名称
        tencent?.shareToQQ(activity, params, listener)
    }

    fun shareNetworkImageToQQ(activity: Activity, imageUrl: String, isShareToQzone: Boolean, listener: IUiListener) {
        Glide.with(activity.baseContext).asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap?>?) {
                    val shareImageFilePath = saveBitmapToFile(activity.baseContext, bitmap)
                    println("base---------shareNetworkImageToQQ--------->shareImageFilePath: $shareImageFilePath")
                    if (isShareToQzone) {
                        shareImageToQZone(activity, shareImageFilePath, listener)
                    } else {
                        shareToQQ(activity, shareImageFilePath, listener)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    fun shareToQQ(activity: Activity, imageUrl: String, listener: IUiListener) {
        if (tencent == null) return
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name))
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl)
        println("base---------shareToQQ--------->")
        tencent?.shareToQQ(activity, params, listener)
    }

    fun shareImageToQZone(activity: Activity, imageFilePath: String, listener: IUiListener) {
        if (tencent == null) return
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name))
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageFilePath)
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN)
        println("base---------shareImageToQZone--------->")
        tencent?.shareToQQ(activity, params, listener)
    }

    /**
     * 分享到QQ空间
     * QzoneShare.SHARE_TO_QZONE_KEY_TYPE  选填 Int    SHARE_TO_QZONE_TYPE_IMAGE_TEXT（图文）
     * QzoneShare.SHARE_TO_QQ_TITLE    必填    分享的标题，最多200个字符。
     * QzoneShare.SHARE_TO_QQ_SUMMARY  选填 String 分享的摘要，最多600字符。
     * QzoneShare.SHARE_TO_QQ_TARGET_URL   必填 String 需要跳转的链接，URL字符串。
     * QzoneShare.SHARE_TO_QQ_IMAGE_URL    选填 String 分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）
     */
    fun shareToQZone(activity: Activity, listener: IUiListener) {
        if (tencent == null) return
        val params = Bundle()
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题") // 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要") // 摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com") // 内容地址
        val imgUrlList = ArrayList<String>()
        imgUrlList.add("https://www.xuebiaogan.com/uploads/allimg/210610/1-210610101235105.jpg")
        imgUrlList.add("https://www.xuebiaogan.com/uploads/allimg/210610/1-210610101235105.jpg")
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList) // 图片地址
        tencent?.shareToQzone(activity, params, listener)
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
        val imageName = "share_to_qq_image.jpg"
//        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), BuildConfig.LIBRARY_PACKAGE_NAME + "Images")
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, imageName)

        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

        return file.absolutePath
    }

}
package com.example.demo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.demo.R
import com.example.demo.tools.QQShareUtils
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError

/**
 * Created by Jack Ke on 2022/3/1
 */
class ZoomImageViewDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zoom_iamge_test_layout)

       val zoomImageView = findViewById<ImageView>(R.id.zoomImageView)
        Glide.with(this)
            .load("https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF")
            .transform()
            .into(zoomImageView)


        zoomImageView.setOnClickListener {
            QQShareUtils.instance.shareNetworkImageToQQ(this@ZoomImageViewDemo, "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF", true, qqShareCallback)
        }
    }


    private val qqShareCallback: IUiListener = object : IUiListener {
        override fun onComplete(o: Any) {
            println("base---qq-------onComplete------->QQ分享成功")
        }

        override fun onError(uiError: UiError) {
            println("base---qq-------onError------->QQ分享失败")
        }

        override fun onCancel() {
            println("base---qq-------onCancel------->QQ分享取消")
        }

        override fun onWarning(code: Int) {
            println("base---qq-------onCancel------->QQ分享onWarning code: $code")
            if (code == Constants.ERROR_NO_AUTHORITY) {
                Toast.makeText(this@ZoomImageViewDemo, "onWarning: 请授权手Q访问分享的文件的读取权限!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
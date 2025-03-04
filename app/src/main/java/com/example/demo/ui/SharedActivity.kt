package com.example.demo.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.demo.R
import com.example.demo.tools.QQShareUtils
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError

/**
 * Created by kec005  on 8/8/24.
 */
class SharedActivity : AppCompatActivity() {
    private val networkImageUrl = "https://i-1.netded.com/2024/0715/f79060bace6e9c13ec1dc662a4e80ce8.jpg"
    private val REQUEST_CODE_PERMISSION: Int = 234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.share_layout)


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 请求读写文件权限
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }

        findViewById<Button>(R.id.qShareImageBtn1).setOnClickListener {
            QQShareUtils.instance.shareNetworkImageToQQ(this@SharedActivity, networkImageUrl, false, qqShareCallback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        QQShareUtils.instance.onActivityResult(requestCode, resultCode, data, qqShareCallback)

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
                Toast.makeText(this@SharedActivity, "onWarning: 请授权手Q访问分享的文件的读取权限!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
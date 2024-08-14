package com.example.demo

import android.app.Application
import com.example.demo.tools.QQShareUtils
import com.example.demo.tools.WeiBoSdkUtil


/**
 * Created by kec005  on 8/2/24.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        QQShareUtils.instance.initTencentQQSdk(applicationContext)
        WeiBoSdkUtil.instance.initWeiBoSdk(applicationContext)
    }

}
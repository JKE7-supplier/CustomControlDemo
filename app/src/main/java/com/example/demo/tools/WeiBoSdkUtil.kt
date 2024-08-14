package com.example.demo.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.openapi.IWBAPI
import com.sina.weibo.sdk.openapi.SdkListener
import com.sina.weibo.sdk.openapi.WBAPIFactory

/**
 * Created by kec005  on 8/2/24.
 */
class WeiBoSdkUtil private constructor() {
    companion object {
        val instance: WeiBoSdkUtil by lazy { WeiBoSdkUtil() }
    }

    private var weiBoApi: IWBAPI? = null

    fun initWeiBoSdk(context: Context) {
        weiBoApi = WBAPIFactory.createWBAPI(context)
//        val authInfo = AuthInfo(context, "APP_KY", "REDIRECT_URL", "SCOPE")
        val authInfo = AuthInfo(context, "547729946", "c29721e08aa1c0d54f486192617ab4b8", "SCOPE")
        weiBoApi?.registerApp(context, authInfo, object : SdkListener {
            override fun onInitSuccess() {
                // SDK初始化成功回调，成功⼀次后再次初始化将不再有任何回调
                println("base-------微博 SDK初始化成功回调----->")
            }

            override fun onInitFailure(p0: Exception?) {
                // SDK初始化失败回调
                println("base-------微博 SDK初始化失败回调----->")
            }
        })
    }

    fun startAuth(activity: Activity) {
        if (weiBoApi == null) return
        weiBoApi?.authorize(activity, object : WbAuthListener {
            override fun onComplete(token: Oauth2AccessToken) {
                println("base------- 微博授权成功----->")
            }

            override fun onError(error: UiError?) {
                println("base------- 微博授权出错----->")
            }

            override fun onCancel() {
                println("base------- 微博授权取消----->")
            }
        })
    }

    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        if (weiBoApi == null) return
        weiBoApi?.authorizeCallback(activity, requestCode, resultCode, data)
    }

}
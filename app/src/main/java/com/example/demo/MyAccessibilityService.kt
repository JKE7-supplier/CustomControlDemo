package com.example.demo

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import java.lang.String


/**
 * Created by kec005  on 8/16/24.
 */
class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        println("base--MyAccessibilityService--------onAccessibilityEvent---------->")
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (rootInActiveWindow != null) {
                // 检查当前活动窗口的根节点，判断是否有其他应用程序的窗口存在
                // 在这里可以执行相应的操作
                val packageName = String.valueOf(event.packageName)
                println("base------------------->getPackageName: ${getPackageName()}    other packageName:$packageName")

                if (!packageName.equals(getPackageName())) {
                    // 发现其他应用程序的窗口存在于当前应用程序的上层
                    // 在这里执行相应的操作
                    println("base------------------->发现其他应用程序的窗口存在于当前应用程序的上层")

                }
            }
        }
    }

    override fun onInterrupt() {

    }
}
package com.example.demo.widget

import android.content.Context
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.demo.R

/**
 * Created by kec005  on 8/16/24.
 */
class MyConstraintLayout: FrameLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        LayoutInflater.from(context).inflate(R.layout.my_constraint_layout, this)
    }

    override fun onFilterTouchEventForSecurity(event: MotionEvent): Boolean {
        if ((event.flags and MotionEvent.FLAG_WINDOW_IS_OBSCURED) == MotionEvent.FLAG_WINDOW_IS_OBSCURED) {
            // show error message
            println("base---------窗口被遮挡------->")
            return false
        }
        println("base--------onFilterTouchEventForSecurity --------")

//        val visibleRect = Rect()
//        val localRect = Rect()
//
//        // 获取当前 View 在屏幕上的可见区域
//        getGlobalVisibleRect(visibleRect)
//
//        // 获取当前 View 在本地坐标系中的可见区域
//        getLocalVisibleRect(localRect)
//
//        if (!visibleRect.intersect(localRect)) {
//            // View 被完全遮挡
//            println("base--------View 被完全遮挡")
//            return false
//        } else if (visibleRect.isEmpty) {
//            // View 完全不可见
//            println("base--------View 完全不可见")
//            return false
//        }

        return super.onFilterTouchEventForSecurity(event)
    }
}
/* (c) Disney. All rights reserved. */
package com.example.demo.tools

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Created by Jack Ke on 2021/9/7
 */
class DashBoardRecyclerSpaceDecoration(
    private val topSpace: Float,
    private val leftSpace: Float
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = dip2px(view.context, topSpace)
        outRect.left = dip2px(view.context, leftSpace)
        //The last one
//        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
//            outRect.right = dip2px(view.context, rightSpace)
//        }
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
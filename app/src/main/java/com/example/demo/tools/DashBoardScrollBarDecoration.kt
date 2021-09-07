/* (c) Disney. All rights reserved. */
package com.example.demo.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Jack Ke on 2021/8/18
 */
class DashBoardScrollBarDecoration : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView) {
        super.onDrawOver(c, parent)

        val barHeight = dip2px(parent.context, 4f)
        val scrollWidth = dip2px(parent.context, 50f)
        val indicatorWidth = dip2px(parent.context, 30f)
        val paddingBottom = dip2px(parent.context, 0f)
        val barX = (parent.width / 2 - scrollWidth / 2).toFloat()
        val barY = (parent.height - paddingBottom - barHeight).toFloat()

        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.parseColor("#79B6FF")
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = barHeight.toFloat()
        c.drawLine(barX, barY, barX + scrollWidth.toFloat(), barY, paint)

        val extent = parent.computeHorizontalScrollExtent()
        val range = parent.computeHorizontalScrollRange()
        val offset = parent.computeHorizontalScrollOffset()
        val maxEndX = (range - extent).toFloat()
        //Whether it can be slid
        if (maxEndX > 0) {
            val proportion = offset / maxEndX
            val scrollableDistance = scrollWidth - indicatorWidth
            val offsetX = scrollableDistance * proportion
            paint.color = Color.parseColor("#6296FF")
            c.drawLine(barX + offsetX, barY, barX + indicatorWidth.toFloat() + offsetX, barY, paint)
        } else {
            paint.color = Color.parseColor("#6296FF")
            c.drawLine(barX, barY, barX + scrollWidth.toFloat(), barY, paint)
        }
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
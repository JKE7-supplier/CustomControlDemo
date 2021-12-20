/* (c) Disney. All rights reserved. */
package com.example.demo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

/**
 * Waterfall stream view
 * Created by Jack Ke on 2021/11/30
 */
class WaterFallLayoutView : ViewGroup {

    private val tag = WaterFallLayoutView::class.java.name
    private val columns = 2
    private var rows = 1
    private val margin = 20
    private var mMaxChildWidth = 0

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context?) : super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mMaxChildWidth = 0
        val childViewCount = childCount
        if (childViewCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        rows = if (childViewCount % columns == 0) childViewCount / columns else childViewCount / columns + 1
        val columnsArray = IntArray(columns)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val child = getChildAt(i * columns + j) ?: break
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                val childHeight = child.measuredHeight + 45
                if (child.visibility == GONE) {
                    continue
                }
                child.measure(
                    MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST)
                )
                columnsArray[j] += childHeight + margin
                mMaxChildWidth = max(mMaxChildWidth, child.measuredWidth)
            }
        }
        setMeasuredDimension(resolveSize(mMaxChildWidth, widthMeasureSpec), resolveSize(getMaxHeight(columnsArray) + margin, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Layout area width
        val width = r - l
        if (childCount == 0) return
        val gridWidth = (width - margin * (columns + 1)) / columns
        var gridHeight: Int
        var left: Int
        val columnsArray = IntArray(columns)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val child = getChildAt(i * columns + j) ?: return
                val childHeight = child.measuredHeight + 45
                child.measure(
                    MeasureSpec.makeMeasureSpec(gridWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST)
                )
//                // If there is an alignment mark at the end, for the bottom alignment
//                if (child.tag != null && child.tag == tag) {
//                    child.measure(
//                        MeasureSpec.makeMeasureSpec(gridWidth, MeasureSpec.EXACTLY),
//                        MeasureSpec.makeMeasureSpec(getMaxHeight(columnsArray) - columnsArray[j], MeasureSpec.AT_MOST)
//                    )
//                    gridHeight = getMaxHeight(columnsArray) - columnsArray[j]
//                    left = j * gridWidth + margin * (j + 1)
//                    child.layout(left, columnsArray[j] + margin, left + gridWidth, columnsArray[j] + gridHeight)
//                    break
//                }
                gridHeight = childHeight
                left = j * gridWidth + margin * (j + 1)
                columnsArray[j] += margin
                child.layout(left, columnsArray[j], left + gridWidth, columnsArray[j] + gridHeight)
                columnsArray[j] += gridHeight
            }
        }
    }

    /**
     * Calculate the overall layout height, in order to be displayed in the nested scrollview
     */
    private fun getMaxHeight(array: IntArray): Int {
        var max = array[0]
        for (i in array) {
            if (max < i) max = i
        }
        return max
    }
}
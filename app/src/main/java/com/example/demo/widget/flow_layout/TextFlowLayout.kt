package com.example.demo.widget.flow_layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.example.demo.R
import java.util.*
import kotlin.math.max

/**
 * Created by lsp on 2017/2/13.
 */
class TextFlowLayout constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private val allChildWidthKey: String = "allChildWidth"
    private val allChildHeightKey: String = "allChildHeight"
    private var viewSpacing: Int = 5
    private var subTextSize: Float = 14f
    private var backgroundRes: Int = R.drawable.text_flow_layout_tag_drawable
    private var subTextColor: Int = R.color.item_sub_text_color
    private var onTabClickListener: OnTabClickListener? = null

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var measuredWidth = 0
        var measuredHeight = 0
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val compute: Map<String, Int> = computeSubView(widthSize - paddingRight)
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = compute[allChildWidthKey]!!
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            measuredHeight = compute[allChildHeightKey]!!
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val rect = getChildAt(i).tag as Rect
            child.layout(rect.left, rect.top, rect.right, rect.bottom)
        }
    }

    /**
     * Measurement process
     *
     * @param flowWidth the width of the view
     * @return Returns the total width and height occupied by child elements (used to calculate the width and height of the AT_MOST mode of Flowlayout)
     */
    private fun computeSubView(flowWidth: Int): Map<String, Int> {
        var aRow = true
        var marginParams: MarginLayoutParams
        var rowsWidth = paddingLeft
        var columnHeight = paddingTop
        var rowsMaxHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val measuredWidth = child.measuredWidth
            val measuredHeight = child.measuredHeight
            marginParams = child.layoutParams as MarginLayoutParams
            val childWidth = marginParams.leftMargin + marginParams.rightMargin + measuredWidth
            val childHeight = marginParams.topMargin + marginParams.bottomMargin + measuredHeight
            rowsMaxHeight = max(rowsMaxHeight, childHeight)
            if (rowsWidth + childWidth > flowWidth) {
                rowsWidth = paddingLeft + paddingRight
                columnHeight += rowsMaxHeight
                rowsMaxHeight = childHeight
                aRow = false
            }
            rowsWidth += childWidth
            child.tag = Rect(
                rowsWidth - childWidth + marginParams.leftMargin,
                columnHeight + marginParams.topMargin,
                rowsWidth - marginParams.rightMargin,
                columnHeight + childHeight - marginParams.bottomMargin
            )
        }
        val flowMap: MutableMap<String, Int> = HashMap()
        if (aRow) flowMap[allChildWidthKey] = rowsWidth else flowMap[allChildWidthKey] = flowWidth
        flowMap[allChildHeightKey] = columnHeight + rowsMaxHeight + paddingBottom
        return flowMap
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }

    @SuppressLint("ResourceAsColor")
    private fun addTextView(text: String?, marginLayoutParams: MarginLayoutParams) {
        val textView = TextView(context)
        textView.textSize = subTextSize
        textView.setTextColor(subTextColor)
        textView.text = text
        textView.tag = text
        addView(textView, marginLayoutParams)
        (textView.layoutParams as MarginLayoutParams).setMargins(
            dp2px(context, viewSpacing.toFloat()), dp2px(context, viewSpacing.toFloat()),
            dp2px(context, viewSpacing.toFloat()), dp2px(context, viewSpacing.toFloat())
        )
        textView.setBackgroundResource(backgroundRes)
        textView.setOnClickListener { onTabClickListener?.onTabClick(textView) }
    }

    fun initData(string: String) {
        val strings = mutableListOf<String>()
        strings.clear()
        strings.add(string)
        initData(strings)
    }

    fun initData(strings: Array<String>) {
        if (strings.isNotEmpty())
            initData(strings.toList())
    }

    @SuppressLint("ResourceAsColor")
    fun initData(list: List<String?>) {
        removeAllViews()
        val marginLayoutParams = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
        for (position in list.indices) {
            addTextView(list[position], marginLayoutParams)
        }
    }

    fun setOnTabClickListener(onTabClickListener: OnTabClickListener?) {
        this.onTabClickListener = onTabClickListener
    }

    fun setViewSpacing(viewSpacing: Int) {
        this.viewSpacing = viewSpacing
    }

    fun setSubViewBackgroundResource(drawable: Int) {
        this.backgroundRes = drawable
    }

    fun setSubTextColor(textColor: Int) {
        this.subTextColor = textColor
    }

    fun setSubTextSize(textSize: Float) {
        this.subTextSize = textSize
    }

    interface OnTabClickListener {
        fun onTabClick(textView: TextView?)
    }

    inner class ViewBean {
        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
    }
}
/* (c) Disney. All rights reserved. */
package com.example.demo.widget.viewpage2

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.demo.R
import com.example.demo.widget.RoundedConstraintLayoutView
import com.example.demo.widget.banner.BaseAutoSwitchedViewPager

/**
 * Created by Jack Ke on 2021/9/22
 */
class SegmentAutoSwitchedViewPage : BaseAutoSwitchedViewPager {
    private lateinit var roundedConstraintLayoutView: RoundedConstraintLayoutView
    private lateinit var viewPager2: ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private var aspectRatio: Float = 0.33f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initView() {
        inflate(context, R.layout.segment_banner_layout, this)
        roundedConstraintLayoutView = findViewById(R.id.roundedConstraintLayoutView)
        viewPager2 = findViewById(R.id.viewPager2)
        indicatorLayout = findViewById(R.id.indicatorLayout)
        viewPager2.setPageTransformer(compositePageTransformer)
        setViewPagerHeightAspectRatio(aspectRatio)
    }

    override fun getViewPager2(): ViewPager2 {
        return viewPager2
    }

    override fun getViewPager2CurrentItem(): Int {
        return viewPager2.currentItem
    }

    override fun setViewPager2CurrentItem(item: Int) {
        viewPager2.currentItem = item
    }

    override fun setViewPager2CurrentItem(item: Int, smoothScroll: Boolean) {
        viewPager2.setCurrentItem(item, smoothScroll)
    }

    override fun getIndicatorLayout(): LinearLayout {
        return indicatorLayout
    }

    /**
     * Set the height ratio of ViewPager
     * @param aspectRatio
     */
    fun setViewPagerHeightAspectRatio(aspectRatio: Float): SegmentAutoSwitchedViewPage {
        viewPager2.post {
            val measuredWidth: Int = viewPager2.measuredWidth
            val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, (measuredWidth * aspectRatio).toInt())
            viewPager2.layoutParams = params
        }
        return this
    }

    /**
     * Set banner rounded corners
     */
    fun setRound(round: Float): SegmentAutoSwitchedViewPage {
        roundedConstraintLayoutView.setRound(round)
        return this
    }
}
package com.example.demo.widget.viewpage2;

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.demo.R
import com.example.demo.widget.RoundedConstraintLayoutView


/**
 * Created by Jack Ke on 2021/9/16
 */
class AutoSwitchedViewPager : RelativeLayout, LifecycleObserver {
    private lateinit var roundedConstraintLayoutView: RoundedConstraintLayoutView
    private lateinit var mIndicatorLayout: LinearLayout
    private lateinit var mViewPager: ViewPager2

    private val mContext: Context = context
    private var mBannerPagerAdapter: BaseAutoSwitchedViewPagerAdapter<Any, RecyclerView.ViewHolder>? = null
    private var mMarginPageTransformer: MarginPageTransformer? = null
    private var mOnPageClickListener: BaseAutoSwitchedViewPagerAdapter.OnPageClickListener? = null
    private var mCompositePageTransformer: CompositePageTransformer? = null

    private var lastPosition = 0
    private var listSize = 0
    private var interval = 3000L
    private var isAutoPlay = false
    private var isLooper = false
    private var isShowIndicator = false
    private var pageMargin = 0
    private var revealWidth = -1//The exposed width of both sides of the page in one-screen multi-page mode
    private var offscreenPageLimit = 3
    private var indicatorMargin = dpToPx(5)
    private var normalImage = R.drawable.preview_banenr_dot_noraml
    private var checkedImage = R.drawable.preview_banenr_dot_selected
    private var aspectRatio: Float = 0.33f

    private val mHandler: Handler = Handler()

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            val currentItem = mViewPager.currentItem
            if (isLooper) {
                mViewPager.currentItem = currentItem + 1
            } else {
                if (currentItem == listSize - 1) {
                    mViewPager.setCurrentItem(0, false)
                } else {
                    mViewPager.currentItem = currentItem + 1
                }
            }
            mHandler.postDelayed(this, interval)
        }
    }

    private val mOnPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            setIndicatorDots(position)
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()

    }

    private fun initView() {
        inflate(context, R.layout.segment_banner_layout, this)
        mViewPager = findViewById(R.id.viewPager2)
        roundedConstraintLayoutView = findViewById(R.id.roundedConstraintLayoutView)
        mIndicatorLayout = findViewById(R.id.bvp_layout_indicator)
        mCompositePageTransformer = CompositePageTransformer()
        mViewPager.setPageTransformer(mCompositePageTransformer)
        setViewPagerHeightAspectRatio(aspectRatio)
    }

    private fun initBannerData(list: List<Any>) {
        if (list.isNotEmpty()) {
            initIndicatorDots(list)
            setupViewPager(list)
        }
    }

    private fun initIndicatorDots(list: List<Any>) {
        mIndicatorLayout.removeAllViews()
        if (isShowIndicator && listSize > 1) {
            mIndicatorLayout.visibility = View.VISIBLE
            for (i in list.indices) {
                val imageView = ImageView(mContext)
                if (i == 0) imageView.setBackgroundResource(checkedImage)
                else imageView.setBackgroundResource(normalImage)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(
                    indicatorMargin,
                    indicatorMargin,
                    indicatorMargin,
                    indicatorMargin
                )
                imageView.layoutParams = layoutParams
                mIndicatorLayout.addView(imageView)
            }
        } else {
            mIndicatorLayout.visibility = View.GONE
        }
    }

    private fun setIndicatorDots(position: Int) {
        if (isShowIndicator && listSize > 1) {
            val current = position % listSize
            val last = lastPosition % listSize
            mIndicatorLayout.getChildAt(current).setBackgroundResource(checkedImage)
            mIndicatorLayout.getChildAt(last).setBackgroundResource(normalImage)
            lastPosition = position
        }
    }

    private fun setupViewPager(list: List<Any>) {
        if (mBannerPagerAdapter == null) {
            throw NullPointerException("You must set adapter for BannerViewPager")
        }
        if (revealWidth != -1) {
            val recyclerView = mViewPager.getChildAt(0) as RecyclerView
            recyclerView.setPadding(pageMargin + revealWidth, 0, pageMargin + revealWidth, 0)
            recyclerView.clipToPadding = false
        }
        mBannerPagerAdapter?.isCanLoop = isLooper
        mBannerPagerAdapter?.pageClickListener = mOnPageClickListener
        mViewPager.adapter = mBannerPagerAdapter
        resetCurrentItem()
        mViewPager.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback)
        mViewPager.offscreenPageLimit = offscreenPageLimit
        startTimer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner?) {
        startTimer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(owner: LifecycleOwner?) {
        stopTimer()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                stopTimer()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                startTimer()
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun startTimer() {
        if (isAutoPlay && mBannerPagerAdapter != null && listSize > 1) {
            stopTimer()
            mHandler.postDelayed(runnable, interval)
        }
    }

    private fun stopTimer() {
        mHandler.removeCallbacks(runnable)
    }

    fun setViewPagerHeightAspectRatio(aspectRatio: Float): AutoSwitchedViewPager {
        mViewPager.post {
            val measuredWidth: Int = mViewPager.measuredWidth
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                (measuredWidth * aspectRatio).toInt()
            )
            mViewPager.layoutParams = params
        }
        return this
    }

    /**
     * If automatic polling is turned on, the life cycle must be set, and it will eat memory
     * @param lifecycleRegistry Lifecycle
     * @return BannerViewPager<T>
     */
    fun setLifecycleRegistry(lifecycleRegistry: Lifecycle): AutoSwitchedViewPager {
        lifecycleRegistry.addObserver(this)
        return this
    }

    /**
     * Set up automatic rotation
     *
     * @param autoPlay
     * @return
     */
    fun setAutoPlay(autoPlay: Boolean): AutoSwitchedViewPager {
        isAutoPlay = autoPlay
        return this
    }

    /**
     * Set whether to loop
     *
     * @param canLoop
     * @return
     */
    fun setCanLoop(canLoop: Boolean): AutoSwitchedViewPager {
        isLooper = canLoop
        return this
    }

    /**
     * Set adapter
     *
     * @param adapter
     * @return
     */
    fun setAdapter(adapter: BaseAutoSwitchedViewPagerAdapter<Any, RecyclerView.ViewHolder>?): AutoSwitchedViewPager {
        mBannerPagerAdapter = adapter
        return this
    }

    /**
     * Set the carousel interval time
     *
     * @param millisecond
     * @return
     */
    fun setInterval(millisecond: Long): AutoSwitchedViewPager {
        interval = millisecond
        return this
    }

    /**
     * Set the number of pages that should remain on either side of the current view
     *
     * @param int
     * @return
     */
    fun setOffscreenPageLimit(int: Int): AutoSwitchedViewPager {
        offscreenPageLimit = int
        return this
    }

    /**
     * Set whether to show the indicator
     *
     */
    fun setCanShowIndicator(bool: Boolean): AutoSwitchedViewPager {
        isShowIndicator = bool
        return this
    }

    /**
     * Setup page converter
     *
     * @param transformer
     * @return
     */
    fun setPageTransformer(transformer: ViewPager2.PageTransformer): AutoSwitchedViewPager {
        mViewPager.setPageTransformer(transformer)
        return this
    }

    /**
     * [transformer] PageTransformer that will modify each page's animation properties
     */
    fun addPageTransformer(transformer: ViewPager2.PageTransformer): AutoSwitchedViewPager {
        mCompositePageTransformer?.addTransformer(transformer)
        return this
    }

    fun removeTransformer(transformer: ViewPager2.PageTransformer) {
        mCompositePageTransformer?.removeTransformer(transformer)
    }

    fun removeMarginPageTransformer() {
        if (mMarginPageTransformer != null) {
            mCompositePageTransformer?.removeTransformer(mMarginPageTransformer!!)
        }
    }

    /**
     * Set page margins
     *
     * @param margin page margin
     */
    fun setPageMargin(margin: Int): AutoSwitchedViewPager {
        pageMargin = dpToPx(margin)
        removeMarginPageTransformer()
        mMarginPageTransformer = MarginPageTransformer(pageMargin)
        mCompositePageTransformer?.addTransformer(mMarginPageTransformer!!)
        return this
    }

    /**
     * The exposed width of both sides of the page in one-screen multi-page mode
     */
    fun setRevealWidth(int: Int): AutoSwitchedViewPager {
        revealWidth = dpToPx(int)
        return this
    }

    /**
     * Set the project click listener
     *
     * @param onPageClickListener item click listener
     */
    fun setOnPageClickListener(onPageClickListener: BaseAutoSwitchedViewPagerAdapter.OnPageClickListener): AutoSwitchedViewPager {
        mOnPageClickListener = onPageClickListener
        return this
    }

    /**
     * Set indicator interval
     *
     */
    fun setIndicatorMargin(margin: Int): AutoSwitchedViewPager {
        indicatorMargin = margin
        return this
    }

    /**
     * Set banner rounded corners
     */
    fun setRound(round: Float): AutoSwitchedViewPager {
        roundedConstraintLayoutView.setRound(round)
        return this
    }

    /**
     * Set indicator picture
     *
     */
    fun setIndicatorSliderColor(
        @DrawableRes normal: Int,
        @DrawableRes checked: Int
    ): AutoSwitchedViewPager {
        normalImage = normal
        checkedImage = checked
        return this
    }

    /**
     * Create with data BannerViewPager
     */
    fun create(data: List<String>) {
        if (mBannerPagerAdapter == null) {
            throw NullPointerException("You must set adapter for BannerViewPager")
        }
        listSize = data.size
        mBannerPagerAdapter?.setData(data)
        initBannerData(data)
    }

    /**
     * Set current item
     *
     * @param item
     * @param smoothScroll
     */
    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        if (isLooper && listSize > 1) {
            val currentItem = mViewPager.currentItem
            val realPosition: Int? = mBannerPagerAdapter?.getRealPosition(currentItem)
            if (currentItem != item) {
                if (item == 0 && realPosition == listSize - 1) {
                    mViewPager.setCurrentItem(currentItem + 1, smoothScroll)
                } else if (realPosition == 0 && item == listSize - 1) {
                    mViewPager.setCurrentItem(currentItem - 1, smoothScroll)
                } else {
                    mViewPager.setCurrentItem(currentItem + (item - realPosition!!), smoothScroll)
                }
            }
        } else {
            mViewPager.setCurrentItem(item, smoothScroll)
        }
    }

    fun refreshData(list: List<String>) {
        if (mBannerPagerAdapter != null && list.isNotEmpty()) {
            stopTimer()
            listSize = list.size
            mBannerPagerAdapter?.setData(list)
            mBannerPagerAdapter?.notifyDataSetChanged()
            resetCurrentItem()
            initIndicatorDots(list)
            startTimer()
        }
    }

    private fun dpToPx(dip: Int): Int {
        return (0.5f + dip * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun resetCurrentItem() {
        if (listSize > 1 && isLooper) {
            lastPosition = Int.MAX_VALUE / 2 - ((Int.MAX_VALUE / 2) % listSize)
            mViewPager.setCurrentItem(lastPosition, false)
        } else {
            mViewPager.setCurrentItem(0, false)
        }
    }
}



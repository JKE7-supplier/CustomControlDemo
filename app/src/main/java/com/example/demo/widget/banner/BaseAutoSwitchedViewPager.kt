package com.example.demo.widget.banner;

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.demo.R


/**
 * Created by Jack Ke on 2021/9/16
 */
abstract class BaseAutoSwitchedViewPager : RelativeLayout, LifecycleObserver {
    private var onPageClickListener: BaseAutoSwitchedViewPagerAdapter.OnPageClickListener? = null
    protected var bannerPagerAdapter: BaseAutoSwitchedViewPagerAdapter<Any, RecyclerView.ViewHolder>? = null
    private var marginPageTransformer: MarginPageTransformer? = null
    protected var compositePageTransformer: CompositePageTransformer? = null

    private var listSize = 0
    private var isAutoPlay = false
    private var isLooper = false
    private var isShowIndicator = false
    private var interval = 3000L
    private var revealWidth = -1//The exposed width of both sides of the page in one-screen multi-page mode
    private var pageMargin = 0
    private var offscreenPageLimit = 3
    private var lastPosition = 0//Record the last position of the carousel
    private var indicatorMargin = dpToPx(5)
    private var normalImage = R.drawable.preview_banenr_dot_normal
    private var checkedImage = R.drawable.preview_banenr_dot_selected

    private val autoSwitchedHandler: Handler = Handler()

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            val currentItem = getViewPager2CurrentItem()
            if (isLooper) {
                setViewPager2CurrentItem(currentItem + 1)
            } else {
                if (currentItem == listSize - 1) {
                    setViewPager2CurrentItem(0, false)
                } else {
                    setViewPager2CurrentItem(currentItem + 1)
                }
            }
            autoSwitchedHandler.postDelayed(this, interval)
        }
    }

    private val onPageChangeCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            onViewPageSelected(position)
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        compositePageTransformer = CompositePageTransformer()
        this.initView()
    }

    abstract fun initView()

    abstract fun getViewPager2(): ViewPager2

    /**
     * viewPager2.getCurrentItem
     */
    abstract fun getViewPager2CurrentItem(): Int

    /**
     * viewPager2.setCurrentItem(int item)
     */
    abstract fun setViewPager2CurrentItem(item: Int)

    /**
     *  viewPager2.setCurrentItem(int item, boolean smoothScroll)
     */
    abstract fun setViewPager2CurrentItem(item: Int, smoothScroll: Boolean)

    abstract fun getIndicatorLayout(): LinearLayout

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner?) = startAutoSwitched()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(owner: LifecycleOwner?) = stopAutoSwitched()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                stopAutoSwitched()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                startAutoSwitched()
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    open fun onViewPageSelected(position: Int) {
        setIndicatorDots(position)
    }

    open fun initBannerData(list: List<Any>) {
        if (list.isNotEmpty()) {
            initIndicatorDots(list)
            setupViewPager()
        }
    }

    private fun startAutoSwitched() {
        if (isAutoPlay && listSize > 1) {
            stopAutoSwitched()
            autoSwitchedHandler.postDelayed(runnable, interval)
        }
    }

    private fun stopAutoSwitched() {
        autoSwitchedHandler.removeCallbacks(runnable)
    }

    private fun setupViewPager() {
        if (bannerPagerAdapter == null) {
            return
        }
        if (getViewPager2().getChildAt(0) is RecyclerView) {
            if (revealWidth != -1) {
                val recyclerView = getViewPager2().getChildAt(0) as RecyclerView
                recyclerView.setPadding(pageMargin + revealWidth, 0, pageMargin + revealWidth, 0)
                recyclerView.clipToPadding = false
            }
            bannerPagerAdapter?.isCanLoop = isLooper
            bannerPagerAdapter?.pageClickListener = onPageClickListener
            getViewPager2().adapter = bannerPagerAdapter
            resetCurrentItem()
            getViewPager2().unregisterOnPageChangeCallback(onPageChangeCallback)
            getViewPager2().registerOnPageChangeCallback(onPageChangeCallback)
            getViewPager2().offscreenPageLimit = offscreenPageLimit
            startAutoSwitched()
        }
    }

    private fun initIndicatorDots(list: List<Any>) {
        if (list.isNotEmpty()) {
            getIndicatorLayout().removeAllViews()
            if (isShowIndicator && listSize > 1) {
                getIndicatorLayout().visibility = View.VISIBLE
                for (i in list.indices) {
                    val imageView = ImageView(context)
                    if (i == 0) imageView.setBackgroundResource(checkedImage) else imageView.setBackgroundResource(normalImage)
                    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin)
                    imageView.layoutParams = layoutParams
                    getIndicatorLayout().addView(imageView)
                }
            } else {
                getIndicatorLayout().visibility = View.GONE
            }
        }
    }

    private fun setIndicatorDots(position: Int) {
        if (isShowIndicator && listSize > 1) {
            val current = position % listSize
            val last = lastPosition % listSize
            getIndicatorLayout().getChildAt(current).setBackgroundResource(checkedImage)
            getIndicatorLayout().getChildAt(last).setBackgroundResource(normalImage)
            lastPosition = position
        }
    }

    private fun resetCurrentItem() {
        if (listSize > 1 && isLooper) {
            lastPosition = Int.MAX_VALUE / 2 - ((Int.MAX_VALUE / 2) % listSize)
            getViewPager2().setCurrentItem(lastPosition, false)
        } else {
            getViewPager2().setCurrentItem(0, false)
        }
    }

    /**
     * Set current item
     *
     * @param item
     * @param smoothScroll
     */
    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        if (isLooper && listSize > 1) {
            val currentItem = getViewPager2().currentItem
            val realPosition: Int? = bannerPagerAdapter?.getRealPosition(currentItem)
            if (currentItem != item) {
                if (item == 0 && realPosition == listSize - 1) {
                    getViewPager2().setCurrentItem(currentItem + 1, smoothScroll)
                } else if (realPosition == 0 && item == listSize - 1) {
                    getViewPager2().setCurrentItem(currentItem - 1, smoothScroll)
                } else {
                    getViewPager2().setCurrentItem(currentItem + (item - realPosition!!), smoothScroll)
                }
            }
        } else {
            getViewPager2().setCurrentItem(item, smoothScroll)
        }
    }

    private fun dpToPx(dip: Int): Int = (0.5f + dip * Resources.getSystem().displayMetrics.density).toInt()

    fun refreshData(list: List<Any>) {
        if (bannerPagerAdapter != null && list.isNotEmpty()) {
            stopAutoSwitched()
            listSize = list.size
            bannerPagerAdapter?.setData(list)
            bannerPagerAdapter?.notifyDataSetChanged()
            resetCurrentItem()
            initIndicatorDots(list)
            startAutoSwitched()
        }
    }

    /**
     * If automatic polling is turned on, the life cycle must be set, and it will eat memory
     * @param lifecycleRegistry Lifecycle
     * @return BannerViewPager<T>
     */
    fun setLifecycleRegistry(lifecycleRegistry: Lifecycle): BaseAutoSwitchedViewPager {
        lifecycleRegistry.addObserver(this)
        return this
    }

    /**
     * Set the number of pages that should remain on either side of the current view
     *
     * @param int
     * @return
     */
    fun setOffscreenPageLimit(int: Int): BaseAutoSwitchedViewPager {
        offscreenPageLimit = int
        return this
    }

    /**
     * Set up automatic rotation
     *
     * @param autoPlay
     * @return
     */
    fun setAutoPlay(autoPlay: Boolean): BaseAutoSwitchedViewPager {
        isAutoPlay = autoPlay
        return this
    }

    /**
     * Set whether to loop
     *
     * @param canLoop
     * @return
     */
    fun setCanLoop(canLoop: Boolean): BaseAutoSwitchedViewPager {
        isLooper = canLoop
        return this
    }

    /**
     * Set whether to show the indicator
     *
     */
    fun setCanShowIndicator(bool: Boolean): BaseAutoSwitchedViewPager {
        isShowIndicator = bool
        return this
    }

    /**
     * Set the carousel interval time
     *
     * @param millisecond
     * @return
     */
    fun setInterval(millisecond: Long): BaseAutoSwitchedViewPager {
        interval = millisecond
        return this
    }

    /**
     * The exposed width of both sides of the page in one-screen multi-page mode
     */
    fun setRevealWidth(int: Int): BaseAutoSwitchedViewPager {
        revealWidth = dpToPx(int)
        return this
    }

    /**
     * Set page margins
     *
     * @param margin page margin
     */
    fun setPageMargin(margin: Int): BaseAutoSwitchedViewPager {
        pageMargin = dpToPx(margin)
        removeMarginPageTransformer()
        marginPageTransformer = MarginPageTransformer(pageMargin)
        compositePageTransformer?.addTransformer(marginPageTransformer!!)
        return this
    }

    /**
     * [transformer] PageTransformer that will modify each page's animation properties
     */
    fun addPageTransformer(transformer: ViewPager2.PageTransformer): BaseAutoSwitchedViewPager {
        compositePageTransformer?.addTransformer(transformer)
        return this
    }

    fun removeTransformer(transformer: ViewPager2.PageTransformer) {
        compositePageTransformer?.removeTransformer(transformer)
    }

    fun removeMarginPageTransformer() {
        marginPageTransformer?.let { compositePageTransformer?.removeTransformer(it) }
    }

    /**
     * Set the project click listener
     *
     * @param onPageClickListener item click listener
     */
    fun setOnPageClickListener(onPageClickListener: BaseAutoSwitchedViewPagerAdapter.OnPageClickListener): BaseAutoSwitchedViewPager {
        this.onPageClickListener = onPageClickListener
        return this
    }

    /**
     * Set adapter
     *
     * @param adapter
     * @return
     */
    fun setViewPageAdapter(adapter: BaseAutoSwitchedViewPagerAdapter<Any, RecyclerView.ViewHolder>?): BaseAutoSwitchedViewPager {
        bannerPagerAdapter = adapter
        return this
    }

    /**
     * 设置页面转换器
     *
     * @param transformer
     * @return
     */
    fun setPageTransformer(transformer: ViewPager2.PageTransformer): BaseAutoSwitchedViewPager {
        getViewPager2().setPageTransformer(transformer)
        return this
    }

    /**
     * Set indicator interval
     *
     */
    fun setIndicatorMargin(margin: Int): BaseAutoSwitchedViewPager {
        indicatorMargin = margin
        return this
    }

    /**
     * Set indicator picture
     *
     */
    fun setIndicatorSliderColor(@DrawableRes normal: Int, @DrawableRes checked: Int): BaseAutoSwitchedViewPager {
        normalImage = normal
        checkedImage = checked
        return this
    }

    /**
     * Create with data BannerViewPager
     */
    fun create(data: List<Any>) {
        if (bannerPagerAdapter == null) {
            return
        }
        listSize = data.size
        bannerPagerAdapter?.setData(data)
        initBannerData(data)
    }
}



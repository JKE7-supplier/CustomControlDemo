package com.example.demo.widget.viewpage2

//import kotlinx.android.synthetic.main.home_page_segment_banner_layout.view.*
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.demo.R
import com.example.demo.adapter.HomePageSegmentBannerAdapter
import com.example.demo.widget.RoundedConstraintLayoutView
import org.jetbrains.annotations.NotNull

/**
 * Created by Jack Ke on 2021/9/16
 */
class HomePageSegmentBannerView : ConstraintLayout {
    lateinit var viewPager2: ViewPager2
    lateinit var indicatorLinearLayout: LinearLayout
    private lateinit var roundedConstraintLayoutView: RoundedConstraintLayoutView

    private var normalImage = R.drawable.preview_banenr_dot_normal//正常指示器图片
    private var checkedImage = R.drawable.preview_banenr_dot_selected//选中指示器图片

    private val mContext: Context = context
    private var interval = 3000L//轮播间隔时间
    private var isLooper = false//是否循环
    private var listSize = 0
    private var isAutoPlay = false//是否自动轮播

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

//    /**
//     * 轮播定时器
//     */
//    private val mHandler: Handler = Handler()
//
//    private val runnable: Runnable = object : Runnable {
//        override fun run() {
//            val currentItem = viewPager2.currentItem
//            if (isLooper) {
//                viewPager2.currentItem = currentItem + 1
//            } else {
//                if (currentItem == listSize - 1) {
//                    viewPager2.setCurrentItem(0, false)
//                } else {
//                    viewPager2.currentItem = currentItem + 1
//                }
//            }
//            mHandler.postDelayed(this, interval)
//        }
//    }

    private val mOnPageChangeCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

        }
    }

    private fun initView(context: Context) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.home_page_segment_banner_layout, this)
        roundedConstraintLayoutView = view.findViewById(R.id.roundedConstraintLayoutView) as RoundedConstraintLayoutView
        indicatorLinearLayout = view.findViewById(R.id.indicatorLinearLayout) as LinearLayout
        viewPager2 = view.findViewById(R.id.viewPager2) as ViewPager2
        viewPager2.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        viewPager2.registerOnPageChangeCallback(mOnPageChangeCallback)
    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                stopTimer()
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
//                startTimer()
//            }
//            else -> {
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    fun setAdapter(@NotNull segmentBannerAdapter: HomePageSegmentBannerAdapter, @NotNull models: List<String>) {
        this.listSize = models.size
        segmentBannerAdapter.setData(models)
        segmentBannerAdapter.isCanLoop = isLooper
        viewPager2.adapter = segmentBannerAdapter
    }

//    private fun startTimer() {
//        if (isAutoPlay && listSize > 1) {
//            stopTimer()
//            mHandler.postDelayed(runnable, interval)
//        }
//    }
//
//    private fun stopTimer() {
//        mHandler.removeCallbacks(runnable)
//    }

    fun setRound(round: Float) {
        roundedConstraintLayoutView.setRound(round)
    }

    /**
     * According to the resolution of the phone, the unit is converted from dp to px (pixel)
     */
    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
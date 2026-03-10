package com.example.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.demo.R
import com.example.demo.combination_widget.HomePreviewBannerView
import com.example.demo.entity.HomePreviewViewModel
import com.example.demo.preview_banner_adapter.HomePageViewPageAdapter
import com.example.demo.preview_banner_adapter.HomePreviewRecyclerViewAdapter
import java.util.*

/**
 * Created by Jack Ke on 2021/9/6
 */
class PreviewBannerActivity : AppCompatActivity() {

    private val strings = arrayOf(
        "https://static-le.shanghaidisneyresort.com/0fbd235203c7e6be/media/d98a5dd264513ff1/DPA-bg.png",
//        "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
        "https://static-le.shanghaidisneyresort.com/0fbd235203c7e6be/media/d98a5dd264513ff1/DPA-bg.png",
        "https://static-le.shanghaidisneyresort.com/0fbd235203c7e6be/media/d98a5dd264513ff1/DPA-bg.png",
        "https://static-le.shanghaidisneyresort.com/0fbd235203c7e6be/media/d98a5dd264513ff1/DPA-bg.png",
        "https://static-le.shanghaidisneyresort.com/0fbd235203c7e6be/media/d98a5dd264513ff1/DPA-bg.png"
    )

    private var homePreviewBannerView: HomePreviewBannerView? = null
    private var homePageViewPageAdapter: HomePageViewPageAdapter? = null
    private var homePreviewRecyclerViewAdapter: HomePreviewRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_banner_activity)
        initView()
    }

    private fun initView() {
        homePreviewBannerView = findViewById(R.id.home_preview_banner_view)
        homePreviewBannerView?.setRound(20f)
        homePreviewBannerView?.setTitleText("Title ......")
        homePreviewBannerView?.setSubTitleText("Sub Title ......")
        homePreviewBannerView?.setTitleTextColor(resources.getColor(R.color.white))
        homePreviewBannerView?.setSubTitleTextColor(resources.getColor(R.color.white))
        homePreviewBannerView?.initIndicatorView(getData().size)
        initHomePageView()
        initRecyclerView()
    }

    private fun initHomePageView() {
        homePreviewBannerView?.viewPage?.offscreenPageLimit = 0
        homePageViewPageAdapter = HomePageViewPageAdapter(getData())
        homePreviewBannerView?.setViewPageAdapter(homePageViewPageAdapter)
        homePreviewBannerView?.viewPage?.currentItem = getData().size * 1000
        homePreviewBannerView?.viewPage?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val newPosition = position % getData().size
                homePreviewBannerView?.updateIndicatorView(newPosition)
                homePreviewBannerView?.setRecyclerViewSmoothScrollToPosition(newPosition)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun initRecyclerView() {
        homePreviewBannerView?.previewConstraintLayout?.post {
            val measuredWidth = homePreviewBannerView?.previewConstraintLayout?.measuredWidth
            homePreviewRecyclerViewAdapter =
                measuredWidth?.let { HomePreviewRecyclerViewAdapter(it, 10) }
            homePreviewRecyclerViewAdapter?.setModels(getData())
            homePreviewBannerView?.recyclerView?.adapter = homePreviewRecyclerViewAdapter
        }

        homePreviewBannerView?.post {
            val homePreviewRecyclerViewAdapter =
                HomePreviewRecyclerViewAdapter(
                    homePreviewBannerView!!.measuredWidth, 10
                )
            homePreviewRecyclerViewAdapter.setModels(getData())
        }
    }

    private fun getData(): ArrayList<HomePreviewViewModel> {
        val lists = ArrayList<HomePreviewViewModel>()
        for (i in strings.indices) {
            val homePreviewViewModel = HomePreviewViewModel()
            homePreviewViewModel.url = strings[i]
            if (i == 0) {
                homePreviewViewModel.isSelected = true
            }
            lists.add(homePreviewViewModel)
        }
        return lists
    }
}
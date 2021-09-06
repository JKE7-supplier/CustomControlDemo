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
        "https://img2.baidu.com/it/u=985113219,1494482408&fm=26&fmt=auto&gp=0.jpg",
        "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
        "https://img0.baidu.com/it/u=4152011503,3905925573&fm=26&fmt=auto&gp=0.jpg",
        "https://img0.baidu.com/it/u=3660568416,600673464&fm=26&fmt=auto&gp=0.jpg",
        "https://img1.baidu.com/it/u=2714088317,436321673&fm=26&fmt=auto&gp=0.jpg",
        "https://img2.baidu.com/it/u=1854160273,3804635891&fm=26&fmt=auto&gp=0.jpg"
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
            lists.add(homePreviewViewModel)
        }
        return lists
    }
}
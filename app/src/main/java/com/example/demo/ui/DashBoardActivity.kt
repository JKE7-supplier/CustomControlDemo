/* (c) Disney. All rights reserved. */
package com.example.demo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.adapter.DashBoardEntranceAdapter
import com.example.demo.combination_widget.HomePageHeaderView
import com.example.demo.tools.DashBoardRecyclerSpaceDecoration
import com.example.demo.tools.DashBoardScrollBarDecoration
import java.util.*

/**
 * Created by Jack Ke on 2021/9/6
 */
class DashBoardActivity : AppCompatActivity() {

    private lateinit var headerView: HomePageHeaderView
    private lateinit var dashBoardEntranceAdapter: DashBoardEntranceAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dash_board_activity)
        headerView = findViewById(R.id.headerView)
        recyclerView = findViewById(R.id.recycler_view)

        initHeaderView()
        initRecyclerView()
    }

    private fun initHeaderView() {
        headerView.setBackgroundImageUrl("https://static-le.shanghaidisneyresort.com/109dd807ac5d37d1/media/9d5138bab2639a08/homepageheader.png")
        headerView.getLeftIconImageView().setImageResource(R.mipmap.ic_launcher)
        headerView.getLeftTitleTextView().text = "left"
        headerView.getRightIconImageView().setImageResource(R.mipmap.ic_launcher)
        headerView.getRightTitleTextView().text = "right"
        headerView.getHeaderTitleTextView().text = "title"
    }

    private fun initRecyclerView() {
        dashBoardEntranceAdapter = DashBoardEntranceAdapter(getCategoryNameList())
        recyclerView.addItemDecoration(DashBoardScrollBarDecoration())
        recyclerView.addItemDecoration(DashBoardRecyclerSpaceDecoration(29f, 10f))
        recyclerView.adapter = dashBoardEntranceAdapter
        dashBoardEntranceAdapter.setOnItemClickListener(object :
            DashBoardEntranceAdapter.DashBoardRecycleItemClickListener {
            override fun itemOnClick(position: Int) {
                Toast.makeText(
                    this@DashBoardActivity,
                    "-------${position}------",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getCategoryNameList(): List<String> {
        val categoryNameList: MutableList<String> = ArrayList()
        categoryNameList.add("????????????")
        categoryNameList.add("??????")
        categoryNameList.add("????????????")
        categoryNameList.add("????????????")
        categoryNameList.add("??????")
        categoryNameList.add("????????????")
        categoryNameList.add("?????????")
        categoryNameList.add("????????????")
        categoryNameList.add("?????????")
        categoryNameList.add("???????????????")
        categoryNameList.add("????????????")
        categoryNameList.add("??????")
        categoryNameList.add("?????????????????????")
        categoryNameList.add("????????????")
        categoryNameList.add("??????")
        categoryNameList.add("????????????")
        categoryNameList.add("??????")
        categoryNameList.add("????????????")
        categoryNameList.add("??????")
        categoryNameList.add("????????????")
        return categoryNameList
    }
}
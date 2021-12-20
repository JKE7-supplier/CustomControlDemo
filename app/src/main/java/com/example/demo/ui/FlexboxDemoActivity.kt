/* (c) Disney. All rights reserved. */
package com.example.demo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.R
import com.example.demo.widget.WaterFallLayoutView


/**
 * Created by Jack Ke on 2021/11/23
 */
class FlexboxDemoActivity : AppCompatActivity() {

    private var waterFallLayout: WaterFallLayoutView? = null
    private var contentLl: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.water_fall_activity)
        waterFallLayout = findViewById(R.id.waterFallLayout)
        contentLl = findViewById(R.id.content_ll)

        initWaterFallLayout()
    }

    private fun initWaterFallLayout() {
        for (index in getDatas().indices) {
            val itemView = buildItemView()
            val titleTextView = itemView.findViewById<TextView>(R.id.text1)
            val subTextView = itemView.findViewById<TextView>(R.id.text2)
            val textTest = itemView.findViewById<TextView>(R.id.text_test_1)
            titleTextView.text = "乐园一日票+奇妙美食套券2233乐园一日票+"
            subTextView.text = getDatas()[index]
            textTest.text = "30000"
            if (index == 1) {
                titleTextView.visibility = View.GONE
                subTextView.visibility = View.GONE
                textTest.visibility = View.GONE
            }
            waterFallLayout?.addView(itemView)
        }
    }

    private fun buildItemView(): View = LayoutInflater.from(this).inflate(R.layout.flexbox_adapter_item, contentLl, false)

    private fun getDatas(): List<String> {
        val list: MutableList<String> = mutableListOf()
        for (index in 0..50) {
            list.add("FlexboxLayout+RecyclerView--$index ---可以实现瀑布流的效果，不需要指定列数。不需要指定列数")
        }
        return list
    }
}
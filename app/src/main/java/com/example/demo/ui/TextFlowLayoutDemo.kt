package com.example.demo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.R
import com.example.demo.widget.flow_layout.TextFlowLayout

/**
 * Created by Jack Ke on 2022/2/18
 */
class TextFlowLayoutDemo : AppCompatActivity() {

    private val context: Context = this

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_flow_layout_activity)
        val flowLayout: TextFlowLayout = findViewById(R.id.flowLayout)

        flowLayout.initData(
            listOf(
                "Welcome--HFGDGFJHGJH", "的方式都放假了可是翻了翻就是等放假啦速度快放假啊老地方见啊老师放假啊了饭卡就舒服多啦送快递放假了", "学习ing", "恋爱ing", "挣钱", "努力ing", "I thick i can"
            )
        )

        flowLayout.setOnTabClickListener(object : TextFlowLayout.OnTabClickListener {
            override fun onTabClick(textView: TextView?) {
                if (textView != null) {
                    Toast.makeText(context, textView.text, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
package com.example.demo.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.demo.R

/**
 * Created by Jack Ke on 2022/3/1
 */
class ZoomImageViewDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zoom_iamge_test_layout)

       val zoomImageView = findViewById<ImageView>(R.id.zoomImageView)
        Glide.with(this)
            .load("https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF")
            .transform()
            .into(zoomImageView)
    }
}
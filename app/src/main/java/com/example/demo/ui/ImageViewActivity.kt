package com.example.demo.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.demo.R
import com.example.demo.widget.RoundedCornerWithBorderImageView

/**
 * Created by Jack Ke on 2021/11/18
 */
class ImageViewActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        val imageView = findViewById<RoundedCornerWithBorderImageView>(R.id.roundImageView)
        val imageView1 = findViewById<RoundedCornerWithBorderImageView>(R.id.roundImageView1)
        val imageView2 = findViewById<RoundedCornerWithBorderImageView>(R.id.roundImageView2)

        imageView.setType(1)
            .setCornerRadius(8)
            .setBorderColor(Color.YELLOW)
            .setBorderWidth(2)

        imageView1.setType(0)
            .setBorderWidth(2)
            .setBorderColor(Color.GRAY)
            .setProgress(90, Color.GREEN)

        imageView2
            .setType(2)
            .setBorderWidth(2)
            .setBorderColor(Color.GRAY)

        Glide.with(this)
            .load("https://img0.baidu.com/it/u=4152011503,3905925573&fm=26&fmt=auto&gp=0.jpg")
            .into(imageView)

        Glide.with(this)
            .load("https://img0.baidu.com/it/u=4152011503,3905925573&fm=26&fmt=auto&gp=0.jpg")
            .into(imageView1)

        Glide.with(this)
            .load("https://img0.baidu.com/it/u=4152011503,3905925573&fm=26&fmt=auto&gp=0.jpg")
            .into(imageView2)
    }

}
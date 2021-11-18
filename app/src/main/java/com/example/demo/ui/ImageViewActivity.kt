package com.example.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.demo.R
import com.example.demo.databinding.ActivityImageViewBinding

/**
 * Created by Jack Ke on 2021/11/18
 */
class ImageViewActivity : AppCompatActivity() {

    private lateinit var dataBindingView:ActivityImageViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBindingView= DataBindingUtil.setContentView(this, R.layout.activity_image_view)

//        dataBindingView.roundImageView.setCornerRadius(20)
    }

}
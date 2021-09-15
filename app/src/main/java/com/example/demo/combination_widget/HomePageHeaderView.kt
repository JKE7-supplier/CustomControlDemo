package com.example.demo.combination_widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.demo.R

/**
 * Created by Jack Ke on 2021/9/8
 */
class HomePageHeaderView : ConstraintLayout {

    private lateinit var leftIconImageView: AppCompatImageView
    private lateinit var rightIconImageView: AppCompatImageView
    private lateinit var leftTitleTextView: AppCompatTextView
    private lateinit var rightTitleTextView: AppCompatTextView
    private lateinit var headerTitleTextView: AppCompatTextView
    private lateinit var headerImageView: AppCompatImageView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.home_page_header_layout, this)
        headerImageView = findViewById(R.id.headerImageView)
        leftIconImageView = findViewById(R.id.leftIconImageView)
        leftTitleTextView = findViewById(R.id.leftTitleTextView)
        rightIconImageView = findViewById(R.id.rightIconImageView)
        rightTitleTextView = findViewById(R.id.rightTitleTextView)
        headerTitleTextView = findViewById(R.id.headerTitleTextView)
    }

    fun setBackgroundImageUrl(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(headerImageView)
    }

    fun getLeftIconImageView() = leftIconImageView

    fun getLeftTitleTextView() = leftTitleTextView

    fun getRightIconImageView() = rightIconImageView

    fun getRightTitleTextView() = rightTitleTextView

    fun getHeaderTitleTextView() = headerTitleTextView
}
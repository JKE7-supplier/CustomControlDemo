package com.example.demo.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.demo.R
import com.example.demo.ui.view_model.CustomViewModelFactory
import com.example.demo.ui.view_model.MVVMViewModel
import com.example.demo.widget.MyConstraintLayout


/**
 * Created by kec005  on 12/19/23.
 */
class MVVMActivity : AppCompatActivity() {

    //    private lateinit var viewModel: MVVMViewModel
    private val REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION = 232
    private var blurOverlay: View? = null


    private val viewModel by lazy {
//        ViewModelProvider(this).get(MVVMViewModel::class.java)
        val viewModelFactory = CustomViewModelFactory("parameter value(这是在MVVMViewModel创建时传递一个参数)")
        ViewModelProvider(this, viewModelFactory).get(MVVMViewModel::class.java)
    }

    private lateinit var contentTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val view = DataBindingUtil.setContentView(this,R.layout.mvvm_layout)
        setContentView(R.layout.mvvm_layout)
        lifecycle.addObserver(viewModel)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION)
        }

        blurOverlay = View(this).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            background = ContextCompat.getDrawable(context, R.drawable.blur_background)
            visibility = View.GONE
            (window.decorView as ViewGroup).addView(this)
        }


        initView()
        initObserve()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SYSTEM_ALERT_WINDOW_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                // Permission granted, you can now show overlays
                println("base------ ermission granted, you can now show overlays------>")
            } else {
                // Permission not granted
                println("base------ Permission not granted------>")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        blurOverlay?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        blurOverlay?.visibility = View.VISIBLE
    }

    private fun initView() {
        val mainView: MyConstraintLayout = findViewById(R.id.mvvmView)
        contentTv = mainView.findViewById(R.id.contentTv)

        mainView.setOnTouchListener { p0, p1 ->
            println("base------onTouch------>")
            false
        }



        contentTv.setOnClickListener {
            viewModel.contentTvOnClick()
        }
    }

    private fun initObserve() {
        viewModel.counterLiveData.observe(this) {
            contentTv.text = it
        }
    }

}

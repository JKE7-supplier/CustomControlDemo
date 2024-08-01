package com.example.demo.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.demo.R
import com.example.demo.ui.view_model.CustomViewModelFactory
import com.example.demo.ui.view_model.MVVMViewModel


/**
 * Created by kec005  on 12/19/23.
 */
class MVVMActivity : AppCompatActivity() {

//    private lateinit var viewModel: MVVMViewModel

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
        initView()
        initObserve()
    }


    private fun initView() {
        contentTv = findViewById(R.id.contentTv)

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

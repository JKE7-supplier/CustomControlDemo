package com.example.demo.ui.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel


/**
 * Created by kec005  on 12/19/23.
 */
class MVVMViewModel(parameter: String) : ViewModel(), LifecycleObserver {

    val counterLiveData: LiveData<String> get() = _counter
    private val _counter = MutableLiveData<String>()

    init {
        println("base---ViewModel---init---->parameter: $parameter")
        _counter.value = "初始值"
    }

    // 在 ViewModel 中观察和响应 Activity 或 Fragment 生命周期
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        // 在这里实现对 Activity 或 Fragment 生命周期回调方法的响应
        // 可以在这里执行一些初始化操作
        println("base---ViewModel---onCreate---->")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        println("base---ViewModel---onDestroy---->")
    }

    override fun onCleared() {
        // 在关联的 Activity 或 Fragment 销毁时调用
        // 可以在这里执行一些清理操作
        super.onCleared()
        println("base---ViewModel---onCleared---->")
    }

    fun contentTvOnClick() {
        _counter.value = "点击了"
    }
}
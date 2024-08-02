package com.example.demo.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by kec005  on 12/19/23.
 */
class CustomViewModelFactory(private val someParameter: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MVVMViewModel::class.java)) {
            return MVVMViewModel(someParameter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
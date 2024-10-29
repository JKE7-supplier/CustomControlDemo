package com.example.demo.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.demo.R
import com.example.demo.ui.view_model.CustomViewModelFactory
import com.example.demo.ui.view_model.MVVMViewModel
import com.github.barteksc.pdfviewer.PDFView


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

//   private val pdfUriStr = "https://static.shanghaidisneyresort.com/web-img-gallery/PVK-20240501.pdf"
   private val pdfUriStr = "https://static.shanghaidisneyresort.com/web-img-gallery/il_paperino.pdf"
    private fun initView() {
        contentTv = findViewById(R.id.contentTv)

        contentTv.setOnClickListener {
            viewModel.contentTvOnClick()
        }

        val pdfView = findViewById<PDFView>(R.id.pdfView)
        pdfView
            .fromAsset("il_paperino.pdf")
//            .fromAsset("pinocchio_village_kitchen.pdf")
            .defaultPage(0)
            .enableAnnotationRendering(true)
            .onLoad {  }
            .onError {
            }
            .load()

//        pdfView
////            .fromAsset("il_paperino.pdf")
//            .fromAsset("pinocchio_village_kitchen.pdf")
//            .enableSwipe(true) // allows to block changing pages using swipe
//            .swipeHorizontal(false)
//            .enableDoubletap(true)
//            .defaultPage(0)
//            .enableAnnotationRendering(false) //呈现注释（例如评论、颜色或表格）
//            .password(null)
//            .scrollHandle(null)
//            .enableAntialiasing(true) // 稍微改善低分辨率屏幕上的渲染效果
//            .spacing(0)// 页面间距（单位：dp）。要定义间距颜色，请设置视图背景
//            .autoSpacing(false) // 添加动态间距以使每个页面都能在屏幕上显示
//            .pageFitPolicy(FitPolicy.WIDTH)
//            .pageSnap(true) // 将页面对齐到屏幕边界
//            .pageFling(false) // 像 ViewPager 一样，使用快速滑动仅更改单个页面
//            .nightMode(false) // 切换夜间模式

//            .defaultPage(0)
//            .enableAnnotationRendering(true)
//            .onLoad {
//                // 可以在加载成功时添加一些日志或者提示
//                Log.d("PDFView", "PDF 加载成功")
//            }
//            .onError {
//                    // 在发生错误时输出日志或者显示错误信息
//                Log.e("PDFView", "加载 PDF 出现错误: ${it.message}")
//            }
//            .load()
    }

    private fun initObserve() {
        viewModel.counterLiveData.observe(this) {
//            contentTv.text = it
        }
    }

}

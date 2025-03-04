package com.example.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.R
import com.example.demo.tools.PdfDownloader
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File


/**
 * Created by kec005  on 12/19/23.
 */
class MVIActivity : AppCompatActivity() {
    //    private val pdfUriStr = "https://static.shanghaidisneyresort.com/web-img-gallery/PVK-20240501.pdf"
    private val pdfUriStr1 = "https://static.shanghaidisneyresort.com/web-img-gallery/aurora-20240501.pdf"

    private var pdfFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdfview_layout)

        val outputFile = File(filesDir, "downloaded_file.pdf")
        PdfDownloader.downloadPdf(pdfUriStr1, outputFile,
            onComplete = {
                this.pdfFile = it
                openPdf(this.pdfFile!!)
            },
            onFailure = {
                println("base------pdf 打开失败------>error message: ${it.message}")
            })
    }

    private fun openPdf(pdfFile: File) {
        println("base---------->absolutePath: ${pdfFile.absolutePath}")
        val pdfView = findViewById<PDFView>(R.id.pdfView)
        pdfView.fromFile(pdfFile)
//        pdfView.fromAsset("pvk.pdf")
            .defaultPage(0)
            .onPageChange { _, _ ->
                {

                }
            }
            .enableAnnotationRendering(true)
            .onLoad { nbPages -> println("base------onLoad-------->nbPages: $nbPages ") }
            .onError {
                println("base------onError-------->message: ${it.message} ")
            }
            .scrollHandle(DefaultScrollHandle(this))
            .spacing(10) // in dp
            .onPageError { page, t -> println("base------onPageError-------->message: ${t?.message} ") }
            .load()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("base-----onDestroy------->")
        this.pdfFile?.let { PdfDownloader.deleteFile(it) }
    }

}
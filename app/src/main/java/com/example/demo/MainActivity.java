package com.example.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.ui.DashBoardActivity;
import com.example.demo.ui.FlexboxDemoActivity;
import com.example.demo.ui.ImageViewActivity;
import com.example.demo.ui.PreviewBannerActivity;
import com.example.demo.ui.TextFlowLayoutDemo;
import com.example.demo.ui.ZoomImageViewDemo;
import com.example.demo.ui.ViewPage2DemoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        findViewById(R.id.preview_banner).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PreviewBannerActivity.class)));
        findViewById(R.id.dash_board_button).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DashBoardActivity.class)));
        findViewById(R.id.view_page2_button).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ViewPage2DemoActivity.class)));
        findViewById(R.id.view_page3_button).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ImageViewActivity.class)));
        findViewById(R.id.button_4).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, FlexboxDemoActivity.class)));
        findViewById(R.id.button_5).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TextFlowLayoutDemo.class)));
        findViewById(R.id.button_6).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ZoomImageViewDemo.class)));
    }
}
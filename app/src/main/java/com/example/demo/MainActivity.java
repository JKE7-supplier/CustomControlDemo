package com.example.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.ui.DashBoardActivity;
import com.example.demo.ui.PreviewBannerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        findViewById(R.id.preview_banner).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, PreviewBannerActivity.class)));
        findViewById(R.id.dash_board_button).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DashBoardActivity.class)));
    }
}
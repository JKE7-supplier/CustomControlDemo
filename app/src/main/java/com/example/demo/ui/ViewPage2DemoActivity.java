/* (c) Disney. All rights reserved. */
package com.example.demo.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.demo.R;
import com.example.demo.adapter.ViewPage2DemoAdapter;
import com.example.demo.widget.RoundedConstraintLayoutView;
import com.example.demo.widget.banner.Banner;
import com.example.demo.widget.banner.IndicatorView;
import com.example.demo.widget.banner.ScaleInTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jack Ke on 2021/9/15
 */
public class ViewPage2DemoActivity extends AppCompatActivity {
    private ViewPager2 viewPage2;
    private String[] strings = {
            "https://img2.baidu.com/it/u=985113219,1494482408&fm=26&fmt=auto&gp=0.jpg",
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
            "https://img0.baidu.com/it/u=4152011503,3905925573&fm=26&fmt=auto&gp=0.jpg",
            "https://img0.baidu.com/it/u=3660568416,600673464&fm=26&fmt=auto&gp=0.jpg",
            "https://img1.baidu.com/it/u=2714088317,436321673&fm=26&fmt=auto&gp=0.jpg",
            "https://img2.baidu.com/it/u=1854160273,3804635891&fm=26&fmt=auto&gp=0.jpg"};
    private RoundedConstraintLayoutView roundedConstraintLayout;
    private ViewPage2DemoAdapter viewPage2DemoAdapter;
    private Banner bottomBanner;
    private Banner bottomBanner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_page2_demo_activity);
        viewPage2 = findViewById(R.id.viewPage2);
        roundedConstraintLayout = findViewById(R.id.roundedConstraintLayout);
        bottomBanner = findViewById(R.id.bottomBanner);
        bottomBanner1 = findViewById(R.id.bottomBanner1);

        viewPage2DemoAdapter = new ViewPage2DemoAdapter();
        viewPage2DemoAdapter.setModels(getImages());

        initView();

        initBottomBanner();
    }

    private void initBottomBanner() {
        bottomBanner.setAutoPlay(true)
                .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
                .setPagerScrollDuration(800)
                .setRoundCorners(20f)
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        System.out.println("base---bottomBanner--------onPageSelected---------->position: " + position);
                    }
                })
                .setAdapter(viewPage2DemoAdapter);

        final IndicatorView indicatorView = new IndicatorView(this)
                .setIndicatorRatio(1f)
                .setIndicatorRadius(2f)
                .setIndicatorSelectedRatio(3)
                .setIndicatorSelectedRadius(2f)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_CIRCLE)
                .setIndicatorColor(Color.GRAY)
                .setIndicatorSelectorColor(Color.WHITE);

        bottomBanner1.setAutoPlay(true)
                .setIndicator(indicatorView)
                .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
                .setPagerScrollDuration(800)
                .setRoundCorners(20f)
                .setPageMargin(40, 0)
                .addPageTransformer(new ScaleInTransformer())
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        System.out.println("base---bottomBanner1--------onPageSelected---------->position: " + position);
                    }
                })
                .setAdapter(viewPage2DemoAdapter);
    }

    private void initView() {
        roundedConstraintLayout.setRound(20);
        viewPage2.setOffscreenPageLimit(2);
        viewPage2.setAdapter(viewPage2DemoAdapter);
        viewPage2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private boolean isScroll;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == getImages().size() - 1 && isScroll) {
                    viewPage2.setCurrentItem(0);
                } else {
                    isScroll = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 1) {
                    isScroll = true;
                }
            }
        });

    }

    private List<String> getImages() {
        return new ArrayList<>(Arrays.asList(strings));
    }
}

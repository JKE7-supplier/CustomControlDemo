package com.example.video_image_banner;

import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HomePreviewBannerView homePreviewBannerView;
    private HomePageViewPageAdapter homePageViewPageAdapter;
    private String mp4Url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private String[] strings = {
            "https://img2.baidu.com/it/u=985113219,1494482408&fm=26&fmt=auto&gp=0.jpg",
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
            "https://img0.baidu.com/it/u=4152011503,3905925573&fm=26&fmt=auto&gp=0.jpg",
            "https://img0.baidu.com/it/u=3660568416,600673464&fm=26&fmt=auto&gp=0.jpg",
            "https://img1.baidu.com/it/u=2714088317,436321673&fm=26&fmt=auto&gp=0.jpg",
            "https://img2.baidu.com/it/u=1854160273,3804635891&fm=26&fmt=auto&gp=0.jpg"
    };

    private CountDownTimer countDownTimer;
    private RecyclerView recyclerView;
    private HomePreviewRecyclerViewAdapter homePreviewRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        homePreviewBannerView = findViewById(R.id.home_preview_banner_view);
        homePreviewBannerView.setRound(20f);
        homePreviewBannerView.setTitleText("Title ......");
        homePreviewBannerView.setSubTitleText("Sub Title ......");
        homePreviewBannerView.setTitleTextColor(getResources().getColor(R.color.white));
        homePreviewBannerView.setSubTitleTextColor(getResources().getColor(R.color.white));
        homePreviewBannerView.initIndicatorView(getData().size());

        initHomePageView();
        initRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer.cancel();
        }
    }

    private void initHomePageView() {
        homePreviewBannerView.getViewPage().setOffscreenPageLimit(0);
        homePageViewPageAdapter = new HomePageViewPageAdapter(getData());
        homePreviewBannerView.setViewPageAdapter(homePageViewPageAdapter);
        homePreviewBannerView.getViewPage().setCurrentItem(getData().size() * 1000);
        homePreviewBannerView.getViewPage().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int newPosition = position % getData().size();
                homePreviewBannerView.updateIndicatorView(newPosition);
                homePreviewBannerView.setRecyclerViewSmoothScrollToPosition(newPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initRecyclerView() {
        homePreviewBannerView.getPreviewConstraintLayout().post(() -> {
            int measuredWidth = homePreviewBannerView.getPreviewConstraintLayout().getMeasuredWidth();
            homePreviewRecyclerViewAdapter = new HomePreviewRecyclerViewAdapter(measuredWidth, 10);
            homePreviewRecyclerViewAdapter.setModels(getData());
            homePreviewBannerView.getRecyclerView().setAdapter(homePreviewRecyclerViewAdapter);
            homePreviewRecyclerViewAdapter.setOnItemClickListener(position -> {
//                ArrayList<HomePreviewViewModel> lists = getData();
//                for (int i = 0; i < lists.size(); i++) {
//                    HomePreviewViewModel homePreviewViewModel = lists.get(i);
//                    homePreviewViewModel.setSelected(i == position);
//                    lists.add(homePreviewViewModel);
//                }
//                homePreviewRecyclerViewAdapter.notifyItemChanged(position);
            });
        });

        recyclerView = findViewById(R.id.recyclerView);
        homePreviewBannerView.post(() -> {
            HomePreviewRecyclerViewAdapter homePreviewRecyclerViewAdapter = new HomePreviewRecyclerViewAdapter(homePreviewBannerView.getMeasuredWidth(), 10);
            homePreviewRecyclerViewAdapter.setModels(getData());
            recyclerView.setAdapter(homePreviewRecyclerViewAdapter);
        });
    }

//    private void startCountDownTimer() {
////
//        countDownTimer = new CountDownTimer(getData().size() * 1000, 3000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                int currentItemPosition = (homePreviewBannerView.getViewPage().getCurrentItem() % getData().size()) + 1;
//                homePreviewBannerView.getViewPage().setCurrentItem(currentItemPosition);
//                homePreviewBannerView.setRecyclerViewSmoothScrollToPosition(currentItemPosition);
//            }
//
//            @Override
//            public void onFinish() {
//                this.start();
//                countDownTimer.onTick(getData().size() * 1000);
//            }
//        }.start();
//    }

    private ArrayList<HomePreviewViewModel> getData() {
        ArrayList<HomePreviewViewModel> lists = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            HomePreviewViewModel homePreviewViewModel = new HomePreviewViewModel();
            homePreviewViewModel.setUrl(strings[i]);
            lists.add(homePreviewViewModel);
        }
        return lists;
    }

}
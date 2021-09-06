/* (c) Disney. All rights reserved. */
package com.example.demo.preview_banner_adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.demo.entity.HomePreviewViewModel;
import com.example.demo.widget.FillParentVideoView;

import java.util.List;

/**
 * Created by Jack Ke on 2021/8/26
 */
public class HomePageViewPageAdapter extends PagerAdapter {

    private List<HomePreviewViewModel> lists;

    public HomePageViewPageAdapter(List<HomePreviewViewModel> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (lists != null && lists.size() > 0) {
            HomePreviewViewModel viewModel = lists.get(position % lists.size());
            if (viewModel.getUrl().endsWith(".mp4")) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                FillParentVideoView videoView = new FillParentVideoView(container.getContext());
                videoView.setLayoutParams(lp);
                videoView.setVideoURI(Uri.parse(viewModel.getUrl()));
                videoView.start();
                container.addView(videoView);
                return videoView;
            } else {
                final ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(container.getContext()).load(viewModel.getUrl())
                        .into(imageView);
                container.addView(imageView);
                return imageView;
            }
        }
        return new ImageView(container.getContext());
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}

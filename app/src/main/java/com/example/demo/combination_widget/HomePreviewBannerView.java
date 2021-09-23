/* (c) Disney. All rights reserved. */
package com.example.demo.combination_widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.demo.R;
import com.example.demo.widget.RoundedConstraintLayoutView;

import java.util.ArrayList;

/**
 * Created by Jack Ke on 2021/8/27
 */
public class HomePreviewBannerView extends LinearLayout {
    private static final String TAG = "HomePreviewBannerView";

    private TextView titleTextView;
    private TextView subTitleTextview;
    private ViewPager viewPage;
    private RoundedConstraintLayoutView roundedConstraintLayoutView;
    private LinearLayout indicatorLinearLayout;
    private ArrayList<TextView> mTextViews;
    private RecyclerView recyclerView;

    public HomePreviewBannerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HomePreviewBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomePreviewBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.home_preview_banner_layout, this);
        roundedConstraintLayoutView = findViewById(R.id.rounded_constraint_layout);
        viewPage = findViewById(R.id.view_page);
        titleTextView = findViewById(R.id.title_textview);
        subTitleTextview = findViewById(R.id.sub_title_textview);
        recyclerView = findViewById(R.id.recycler_view);
        indicatorLinearLayout = findViewById(R.id.indicator_linear_layout);
    }

    public ConstraintLayout getPreviewConstraintLayout() {
        return roundedConstraintLayoutView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerViewSmoothScrollToPosition(int position) {
        if (recyclerView == null) return;
        recyclerView.smoothScrollToPosition(position);
    }

    public void setRound(float round) {
        if (roundedConstraintLayoutView != null)
            roundedConstraintLayoutView.setRound(round);
    }

    public ViewPager getViewPage() {
        return viewPage;
    }

    public void setViewPageAdapter(PagerAdapter pageAdapter) {
        if (viewPage == null || pageAdapter == null) return;
        viewPage.setAdapter(pageAdapter);
    }

    public void setTitleText(String titleText) {
        if (titleTextView != null) {
            titleTextView.setText(titleText);
        }
    }

    public void setTitleTextColor(@ColorInt int titleTextColor) {
        if (titleTextView != null) {
            titleTextView.setTextColor(titleTextColor);
        }
    }

    public void setTitleTextSize(float titleTextSize) {
        if (titleTextView != null) {
            titleTextView.setTextSize(titleTextSize);
        }
    }

    public void setSubTitleText(String subTitleText) {
        if (subTitleTextview != null) {
            subTitleTextview.setText(subTitleText);
        }
    }

    public void setSubTitleTextColor(@ColorInt int subTitleTextColor) {
        if (subTitleTextview != null) {
            subTitleTextview.setTextColor(subTitleTextColor);
        }
    }

    public void setSubTitleTextSize(float subTitleTextSize) {
        if (subTitleTextview != null) {
            subTitleTextview.setTextSize(subTitleTextSize);
        }
    }

    public void initIndicatorView(int indicatorCount) {
        mTextViews = new ArrayList<>();
        for (int i = 0; i < indicatorCount; i++) {
            TextView textView = new TextView(getContext());
            if (i == 0) {
                textView.setBackgroundResource(R.drawable.preview_banenr_dot_selected);
            } else {
                textView.setBackgroundResource(R.drawable.preview_banenr_dot_normal);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(15), dip2px(5));
            params.setMargins(dip2px(3), dip2px(10), dip2px(3), dip2px(10));
            textView.setLayoutParams(params);
            mTextViews.add(textView);
            if (indicatorLinearLayout != null)
                indicatorLinearLayout.addView(textView);
        }
    }

    public void updateIndicatorView(int position) {
        if (mTextViews != null) {
            for (int i = 0; i < mTextViews.size(); i++) {
                if (position == i) {
                    mTextViews.get(i).setBackgroundResource(R.drawable.preview_banenr_dot_selected);
                } else {
                    mTextViews.get(i).setBackgroundResource(R.drawable.preview_banenr_dot_normal);
                }
            }
        }
    }

    /**
     * According to the resolution of the phone, the unit is converted from dp to px (pixel)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * According to the resolution of the phone, the unit is converted from px (pixel) to dp
     */
    public int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private static class HomePreviewRecyclerSpaceDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public HomePreviewRecyclerSpaceDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = parent.getChildAdapterPosition(view) == 0 ? outRect.left = space * 2 : space;
            outRect.right = parent.getChildAdapterPosition(view) == parent.getChildCount() + 1 ? outRect.right = space * 2 : space;
        }
    }
}

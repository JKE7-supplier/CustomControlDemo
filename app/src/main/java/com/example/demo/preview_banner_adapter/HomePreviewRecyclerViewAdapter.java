/* (c) Disney. All rights reserved. */
package com.example.demo.preview_banner_adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demo.R;
import com.example.demo.entity.HomePreviewViewModel;
import com.example.demo.widget.RoundedCornerImageView;

import java.util.List;

/**
 * Created by Jack Ke on 2021/8/27
 */
public class HomePreviewRecyclerViewAdapter extends RecyclerView.Adapter<HomePreviewRecyclerViewAdapter.HomePreviewViewHolder> {
    private static final int AVERAGE_NUMBER = 5;

    private List<HomePreviewViewModel> models;
    private int parentLayoutWidth;
    private int itemSpacing;
    private OnItemClickListener onItemClickListener;
    private int selectIndex;

    public HomePreviewRecyclerViewAdapter(int parentLayoutWidth, int itemSpacing) {
        this.parentLayoutWidth = parentLayoutWidth;
        this.itemSpacing = itemSpacing;
    }

    private void setSelectIndex(int position) {
        this.selectIndex = position;
        notifyDataSetChanged();
    }

    public void setModels(List<HomePreviewViewModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull HomePreviewViewHolder holder, int position) {
        HomePreviewViewModel homePreviewViewModel = models.get(position);
        if (holder.itemView instanceof RoundedCornerImageView && homePreviewViewModel != null) {
            RoundedCornerImageView circleImageView = (RoundedCornerImageView) holder.itemView;
            circleImageView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            });
            circleImageView.setBorderColor(position == 0 ? Color.WHITE : Color.TRANSPARENT);
            Glide.with(circleImageView.getContext())
                    .load(homePreviewViewModel.getUrl())
                    .into(circleImageView)
                    .onLoadStarted(circleImageView.getResources().getDrawable(R.drawable.ic_launcher_background));
        }
    }

    @Override
    public HomePreviewViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
        RoundedCornerImageView imageView = new RoundedCornerImageView(container.getContext());
        if (parentLayoutWidth > 0) {
            int itemWidth = (int) (parentLayoutWidth / AVERAGE_NUMBER) - itemSpacing * 2;
            int itemHeight = (int) ((int) itemWidth * 0.75);
            //设置 recyclerView itemView Width， 一行显示5个的关键。
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHeight);
            params.setMargins(itemSpacing, 0, itemSpacing, 0);
            imageView.setLayoutParams(params);
        }
        return new HomePreviewViewHolder(imageView);
    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : models.size();
    }

    protected static class HomePreviewViewHolder extends RecyclerView.ViewHolder {
        public HomePreviewViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}

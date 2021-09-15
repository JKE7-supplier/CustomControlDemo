/* (c) Disney. All rights reserved. */
package com.example.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Jack Ke on 2021/9/15
 */
public class ViewPage2DemoAdapter extends RecyclerView.Adapter<ViewPage2DemoAdapter.ViewHolder> {
    private List<String> models;

    @NonNull
    @Override
    public ViewPage2DemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.itemView instanceof ImageView) {
            ImageView imageView = (ImageView) holder.itemView;
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            Glide.with(imageView.getContext())
                    .load(models.get(position))
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setModels(List<String> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

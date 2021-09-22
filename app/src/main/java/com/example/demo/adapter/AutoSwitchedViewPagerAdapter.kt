package com.example.demo.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demo.R
import com.example.demo.widget.viewpage2.BaseAutoSwitchedViewPagerAdapter

class AutoSwitchedViewPagerAdapter<T> : BaseAutoSwitchedViewPagerAdapter<T, AutoSwitchedViewPagerAdapter.ViewHolder>() {

    override fun getLayoutId(viewType: Int) = R.layout.item_banner_samll

    override fun onBind(holder: ViewHolder, data: T, position: Int, pageSize: Int) {
//        holder.imageView.setImageResource(data)
        Glide.with(holder.imageView.context).load(data).into(holder.imageView)
//        Glide.with(holder.imageView.context).load(data).apply(RequestOptions().transform(RoundedCorners(20))).into(holder.imageView)
    }

    override fun createViewHolder(parent: ViewGroup, itemView: View, viewType: Int): ViewHolder {
        return ViewHolder(itemView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.iv_banner)
    }


}
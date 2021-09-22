package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demo.R

/**
 *  轮播控件所需的 BaseAdapter
 */
class HomePageSegmentBannerAdapter: RecyclerView.Adapter<HomePageSegmentBannerAdapter.ViewHolder>() {
    var mList: MutableList<String> = mutableListOf()
    var isCanLoop = true
    var pageClickListener: OnPageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_auto_switch_view_page_samll, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val realPosition: Int = getRealPosition(position)
        Glide.with(holder.imageView.context)
            .load(mList[realPosition])
            .transform()
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            pageClickListener?.onPageClick(realPosition)
        }
    }

    override fun getItemCount(): Int {
        return if (isCanLoop && mList.size > 1) {
            Int.MAX_VALUE
        } else {
            mList.size
        }
    }

    fun getData(): List<String> {
        return mList
    }

    fun setData(list: List<String>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun getListSize(): Int {
        return mList.size
    }

    fun getRealPosition(position: Int): Int {
        val pageSize = mList.size
        if (pageSize == 0) {
            return 0
        }
        return if (isCanLoop) (position + pageSize) % pageSize else position
    }

    interface OnPageClickListener {
        fun onPageClick(position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
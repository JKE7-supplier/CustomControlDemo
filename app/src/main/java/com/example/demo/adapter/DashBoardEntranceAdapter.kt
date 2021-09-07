/* (c) Disney. All rights reserved. */
package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R


/**
 * Created by Jack Ke on 2021/8/17
 */
class DashBoardEntranceAdapter(
    private val lists: List<String>?
) : RecyclerView.Adapter<DashBoardEntranceAdapter.ViewHolder>() {

    private lateinit var dashBoardRecycleItemClickListener: DashBoardRecycleItemClickListener

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_entrance_classification, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.nameTextView.text = lists?.get(position) ?: "--"
        viewHolder.itemView.setOnClickListener { dashBoardRecycleItemClickListener.itemOnClick(position) }
    }

    override fun getItemCount(): Int {
        return lists?.size ?: 0
    }

    fun setOnItemClickListener(dashBoardRecycleItemClickListener: DashBoardRecycleItemClickListener) {
        this.dashBoardRecycleItemClickListener = dashBoardRecycleItemClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameTextView: TextView = view.findViewById(R.id.nameTextView)
        var classificationIconImageView: ImageView = view.findViewById(R.id.classificationIconImageView)
    }

    interface DashBoardRecycleItemClickListener {
        fun itemOnClick(position: Int)
    }
}
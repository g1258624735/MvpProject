package com.example.administrator.myapplication.main

import android.content.Context

import com.example.administrator.myapplication.R
import com.example.administrator.myapplication.base.BaseRecyclerAdapter
import com.example.administrator.myapplication.base.RecyclerViewHolder

/**
 * Created by gxj on 2017/12/13.
 * 首页适配器
 */

class HomeAdapter(ctx: Context, list: MutableList<String>) : BaseRecyclerAdapter<String>(ctx, list) {

    override fun getItemLayoutId(viewType: Int): Int = R.layout.adapter_activity_main_item

    override fun bindData(holder: RecyclerViewHolder, position: Int, item: String) {
        if (item != null) {
            holder.setText(R.id.tv, item)
        }

    }
}

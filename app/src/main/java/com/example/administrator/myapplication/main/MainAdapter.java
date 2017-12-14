package com.example.administrator.myapplication.main;

import android.content.Context;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseRecyclerAdapter;
import com.example.administrator.myapplication.base.RecyclerViewHolder;

import java.util.List;

/**
 * Created by gxj on 2017/12/13.
 * 首页适配器
 */

public class MainAdapter extends BaseRecyclerAdapter<String> {
    public MainAdapter(Context ctx, List<String> list) {
        super(ctx, list);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_activity_main_item;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, String item) {
        if (item != null) {
            holder.setText(R.id.tv, item);
        }

    }
}

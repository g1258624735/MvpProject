package com.example.administrator.myapplication.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.internal.BaselineLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.administrator.myapplication.R
import com.example.administrator.myapplication.base.BaseFragment
import com.example.administrator.myapplication.base.BaseRecyclerAdapter
import com.example.administrator.myapplication.base.RecyclerViewHolder
import com.example.administrator.myapplication.dialog.CommonDialog
import com.example.administrator.myapplication.middle.MiddleFragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.regex.Pattern

/**
 * Android中使用RecyclerView + SnapHelper实现类似ViewPager效果
 */
class SnapHelperFragment : BaseFragment() {

    override fun onCreateView(): View = layoutInflater.inflate(R.layout.fragment_snaphelper, null)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        val manager = LinearLayoutManager(activity as Context?)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        val adapter = MyAdapter(activity, mutableListOf())
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = adapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        val list = mutableListOf<String>()
        (0..20).mapTo(list) { it.toString() }
        adapter.addAll(list)
    }


    class MyAdapter(ctx: Context, list: MutableList<String>) : BaseRecyclerAdapter<String>(ctx, list) {

        override fun getItemLayoutId(viewType: Int): Int = R.layout.adapter_fragment_snaphelper_item

        override fun bindData(holder: RecyclerViewHolder, position: Int, item: String) {
            holder.setText(R.id.tv, item)
            when (position % 3) {
                0 -> {
                    (holder.getTextView(R.id.tv)).setBackgroundColor(Color.GREEN)
                }
                1 -> {
                    (holder.getTextView(R.id.tv)).setBackgroundColor(Color.RED)
                }
                2 -> {
                    (holder.getTextView(R.id.tv)).setBackgroundColor(Color.LTGRAY)
                }
            }

        }

    }
}

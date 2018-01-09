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
import android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.administrator.myapplication.main.SnapHelperFragment.ItemTouchHelperAdapter


/**
 * Android中使用RecyclerView + SnapHelper实现类似ViewPager效果
 * 实现了 RecyclerView 的侧滑和长按拖拽替换位置的功能功能
 */
class SnapHelperFragment : BaseFragment() {

    override fun onCreateView(): View = layoutInflater.inflate(R.layout.fragment_snaphelper, null)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        val manager = LinearLayoutManager(activity as Context?)
        manager.orientation = LinearLayoutManager.VERTICAL
        val adapter = MyAdapter(activity, mutableListOf())
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = adapter
        val snapHelper = PagerSnapHelper()
//        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        //先实例化Callback
        val callback = SimpleItemTouchHelperCallback(adapter)
        //用Callback构造ItemtouchHelper
        val touchHelper = ItemTouchHelper(callback)
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(recyclerView)

        val list = mutableListOf<String>()
        (0..20).mapTo(list) { it.toString() }
        adapter.addAll(list)
        adapter.notifyDataSetChanged()
    }


    class MyAdapter(ctx: Context, list: MutableList<String>) : BaseRecyclerAdapter<String>(ctx, list), ItemTouchHelperAdapter {
        override fun onItemMove(fromPosition: Int, toPosition: Int) {
            //交换位置
            Collections.swap(getData(), fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemDismiss(position: Int) {
            //移除数据
            getData().removeAt(position)
            notifyItemRemoved(position)
        }

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

    interface ItemTouchHelperAdapter {
        //数据交换
        fun onItemMove(fromPosition: Int, toPosition: Int)

        //数据删除
        fun onItemDismiss(position: Int)
    }

    inner class SimpleItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.LEFT
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun isLongPressDragEnabled(): Boolean = true

        override fun isItemViewSwipeEnabled(): Boolean = true

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
                mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }
}

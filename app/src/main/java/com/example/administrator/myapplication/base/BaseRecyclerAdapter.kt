package com.example.administrator.myapplication.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * @author gxj
 * @date 2016-10-19
 */

abstract class BaseRecyclerAdapter<T>(private val mContext: Context, list: MutableList<T>?) : RecyclerView.Adapter<RecyclerViewHolder>() {
    private val mData: MutableList<T> = arrayListOf()
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mClickListener: OnItemClickListener? = null
    private var mLongClickListener: OnItemLongClickListener? = null

    init {
        if (list != null && list.size > 0) {
            mData.addAll(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val holder = RecyclerViewHolder(mContext,
                mInflater.inflate(getItemLayoutId(viewType), parent, false))
        if (mClickListener != null) {
            holder.itemView.setOnClickListener { mClickListener!!.onItemClick(holder.itemView, holder.layoutPosition) }
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener {
                mLongClickListener!!.onItemLongClick(holder.itemView, holder.layoutPosition)
                true
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        bindData(holder, position, mData[position])
    }

    fun getItem(pos: Int): T {
        return mData[pos]
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun add(pos: Int, item: T) {
        mData.add(pos, item)
        notifyItemInserted(pos)
    }

    fun delete(pos: Int) {
        mData.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mLongClickListener = listener
    }

    abstract fun getItemLayoutId(viewType: Int): Int

    abstract fun bindData(holder: RecyclerViewHolder, position: Int, item: T)

    interface OnItemClickListener {
        fun onItemClick(itemView: View, pos: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(itemView: View, pos: Int)
    }
}

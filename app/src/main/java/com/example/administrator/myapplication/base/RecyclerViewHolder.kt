package com.example.administrator.myapplication.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

/**
 * @author gxj
 * @date 2016-10-19
 */

class RecyclerViewHolder(ctx: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mViews: SparseArray<View>

    init {
        mViews = SparseArray()
    }

    private fun <T : View> findViewById(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    fun getView(viewId: Int): View {
        return findViewById(viewId)
    }

    fun getTextView(viewId: Int): TextView {
        return getView(viewId) as TextView
    }

    fun getButton(viewId: Int): Button {
        return getView(viewId) as Button
    }

    fun getImageView(viewId: Int): ImageView {
        return getView(viewId) as ImageView
    }

    fun getImageButton(viewId: Int): ImageButton {
        return getView(viewId) as ImageButton
    }

    fun getEditText(viewId: Int): EditText {
        return getView(viewId) as EditText
    }

    fun setText(viewId: Int, value: String): RecyclerViewHolder {
        val view = findViewById<TextView>(viewId)
        view.text = value
        return this
    }

    fun setBackground(viewId: Int, resId: Int): RecyclerViewHolder {
        val view = findViewById<View>(viewId)
        view.setBackgroundResource(resId)
        return this
    }

    fun setClickListener(viewId: Int, listener: View.OnClickListener): RecyclerViewHolder {
        val view = findViewById<View>(viewId)
        view.setOnClickListener(listener)
        return this
    }
}

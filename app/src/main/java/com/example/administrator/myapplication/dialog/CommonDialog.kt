package com.example.administrator.myapplication.dialog

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import com.example.administrator.myapplication.R

/**
 * Created by gxj on 2017/9/4.
 * 通用dialog
 * 三种通用dialog 显示样式
 * 1-> 只显示文本
 * 2-> 显示输入框
 * 3-> 显示列表样式
 */
open class CommonDialog(context: Context) : BaseDialog(context), View.OnClickListener {
    private var tv_title: TextView? = null
    private var tvContent: TextView? = null
    private var tv_content_desc: TextView? = null
    private var linear_bottom: LinearLayout? = null
    private var linear_in_view: LinearLayout? = null
    private var tv_cancel: TextView? = null
    private var tv_sure: TextView? = null
    private var tv_one_sure: TextView? = null
    private var et_content: EditText? = null
    private var leftText: String? = null
    private var rightText: String? = null
    private var content: String? = null
    private var title: String? = null
    private var etHint: String? = null
    private var desc: String? = null
    private var view: View? = null//自定义View
    private var linear_content: LinearLayout? = null//dialog 的主View
    private var linear_all_bottom: LinearLayout? = null//dialog 的主View

    override var gravity = DialogGravityType.GRAVITY_CENTER

    override var animations = DialogAnimationType.DIALOG_ANIMATION_SCALE
    override var widthStyle = DialogWidthStyle.WIDTH_STYLE_WRAP_CONTENT


    /**
     * 是否显示一个button
     * 1 -> 只有一个确定按钮
     * 2-> 显示确定和取消按钮
     */
    private var isShowOneButton = false
    /**
     * 是否为输入框模式
     * true -> 输入框模式
     * false -> 文本模式
     */
    private var isShowEditText = false
    private var leftOnClickListener: OnCommonClickListener? = null
    private var rightOnClickListener: OnCommonClickListener? = null
    private var oneButtonSureOnClickListener: OnCommonClickListener? = null

    override val layoutId: Int
        get() = R.layout.dialog_common_layout


    /**
     * dialog的宽度样式选择
     */
    interface DialogWidthStyle {
        companion object {
            /**
             * dialog 宽度 WRAP_CONTENT
             */
            val WIDTH_STYLE_WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT
            /**
             * dialog 宽度 WRAP_CONTENT
             */
            val WIDTH_STYLE_MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT
        }
    }

    /**
     * 位置显示样式选择
     */
    interface DialogGravityType {
        companion object {
            /**
             * dialog 位置显示样式
             * 底部显示
             */
            val GRAVITY_BOTTOM = Gravity.BOTTOM
            /**
             * dialog 中部显示
             */
            val GRAVITY_CENTER = Gravity.CENTER
            /**
             * dialog 顶部显示
             */
            val GRAVITY_TOP = Gravity.TOP
        }
    }

    /**
     * 位置显示样式选择
     */
    interface DialogAnimationType {
        companion object {
            /**
             * dialog 弹出动画
             * 渐变显示
             */
            val DIALOG_ANIMATION_SCALE = R.style.DialogScaleAnimation
            /**
             * dialog 弹出动画
             * 从底部弹出 动画
             */
            val DIALOG_ANIMATION_UP_DOWN = R.style.DataSheetAnimation
        }
    }


    override fun initView() {
        tv_title = findViewById(R.id.tv_title)
        tvContent = findViewById(R.id.tv_content)
        tv_content_desc = findViewById(R.id.tv_content_desc)
        linear_bottom = findViewById(R.id.ll_container)
        tv_cancel = findViewById(R.id.tv_cancel)
        tv_sure = findViewById(R.id.tv_sure)
        tv_one_sure = findViewById(R.id.tv_one_sure)
        et_content = findViewById(R.id.et_content)
        linear_in_view = findViewById(R.id.linear_in_view)
        linear_content = findViewById(R.id.linear_content)
        linear_all_bottom = findViewById(R.id.linear_bottom)
        initUI()
    }

    /**
     * linear_content 和 linear_in_view 两个View 的显示隐藏是相对的
     */
    private fun initUI() {
        linear_content?.visibility = if (view != null) View.GONE else View.VISIBLE
        linear_all_bottom?.visibility = if (view != null) View.GONE else View.VISIBLE
        linear_in_view?.visibility = if (view != null) View.VISIBLE else View.GONE
        linear_bottom!!.visibility = if (isShowOneButton) View.GONE else View.VISIBLE
        tv_one_sure!!.visibility = if (isShowOneButton) View.VISIBLE else View.GONE
        tvContent!!.visibility = if (isShowEditText) View.GONE else View.VISIBLE
        et_content!!.visibility = if (isShowEditText) View.VISIBLE else View.GONE
        if (view != null) {
            linear_in_view?.removeAllViews()
            linear_in_view?.addView(view, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        }
        if (!TextUtils.isEmpty(title)) {
            tv_title!!.text = title
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent!!.text = content
        }
        if (!TextUtils.isEmpty(desc)) {
            tv_content_desc!!.text = desc
        }
        if (!TextUtils.isEmpty(leftText)) {
            tv_cancel!!.text = leftText
        }
        if (!TextUtils.isEmpty(rightText)) {
            tv_sure!!.text = rightText
        }
        if (!TextUtils.isEmpty(rightText)) {
            tv_one_sure!!.text = rightText
        }
        if (!TextUtils.isEmpty(etHint)) {
            et_content!!.hint = etHint
        }
    }

    override fun initListener() {
        tv_cancel!!.setOnClickListener(this)
        tv_one_sure!!.setOnClickListener(this)
        tv_sure!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel//取消
            -> {
                dismiss()
                if (leftOnClickListener != null) {
                    leftOnClickListener!!.onClick(this, et_content!!.text.toString().trim { it <= ' ' })
                }
            }
            R.id.tv_sure//确定
            -> if (rightOnClickListener != null) {
                rightOnClickListener!!.onClick(this, et_content!!.text.toString().trim { it <= ' ' })
            }
            R.id.tv_one_sure//只有一个确定按钮样式
            -> if (oneButtonSureOnClickListener != null) {
                oneButtonSureOnClickListener!!.onClick(this, et_content!!.text.toString().trim { it <= ' ' })
            }
        }
    }

    interface OnCommonClickListener {
        fun onClick(dialog: CommonDialog, text: String)
    }

    /**
     * dialog 属性构建者
     */
    class Builder(context: Context) {
        private val dialog: CommonDialog = CommonDialog(context)

        /**
         * 取消按钮文字
         */
        fun setLeftText(leftText: String): Builder {
            dialog.leftText = leftText
            return this
        }

        /**
         * 确认按钮文字
         */
        fun setRightText(rightText: String): Builder {
            dialog.rightText = rightText
            return this
        }

        /**
         * 正文
         */
        fun setContent(content: String): Builder {
            dialog.content = content
            return this
        }

        /**
         * 描述文字
         */
        fun setDes(desc: String): Builder {
            dialog.desc = desc
            return this
        }

        /**
         * 是否只显示一个确定 按钮
         */
        fun setEnableOneButton(isOneButton: Boolean): Builder {
            dialog.isShowOneButton = isOneButton
            return this
        }

        /**
         * 设置dialog 显示屏幕中的位置
         */
        fun setGravity(gravity: Int): Builder {
            dialog.gravity = gravity
            return this
        }

        /**
         * 设置dialog 弹出动画
         */
        fun setAnimationStyle(animationStyle: Int): Builder {
            dialog.animations = animationStyle
            return this
        }

        /**
         * 设置dialog 宽度
         */
        fun setWidthStyle(widthStyle: Int): Builder {
            dialog.widthStyle = widthStyle
            return this
        }

        /**
         * 是否为输入框模式
         * 输入框模式下会隐藏内容文本
         */
        fun setShowEditText(isShowEditText: Boolean): Builder {
            dialog.isShowEditText = isShowEditText
            return this
        }

        /**
         * 标题
         */
        fun setTitle(title: String): Builder {
            dialog.title = title
            return this
        }

        /**
         * 输入框提示语
         */
        fun setEditTextHint(hint: String): Builder {
            dialog.etHint = hint
            return this
        }

        /**
         * 左按钮点击事件
         */
        fun setLeftOnClickListener(leftOnClickListener: OnCommonClickListener): Builder {
            dialog.leftOnClickListener = leftOnClickListener
            return this
        }

        /**
         * 右按钮点击事件
         */
        fun setRightOnClickListener(rightOnClickListener: OnCommonClickListener): Builder {
            dialog.rightOnClickListener = rightOnClickListener
            return this
        }

        /**
         * 只有一个确定按钮的点击事件
         */
        fun setOneButtonSureOnClickListener(oneButtonSureOnClickListener: OnCommonClickListener): Builder {
            dialog.oneButtonSureOnClickListener = oneButtonSureOnClickListener
            return this
        }

        /**
         * 传入一个自定义布局View
         * 扩展dialog 的布局样式
         */
        fun setView(view: View): Builder {
            dialog.view = view
            return this
        }

        /**
         * 返回dialog
         *
         * @return CommonDialog
         */
        fun create(): CommonDialog = dialog

    }
}

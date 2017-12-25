package com.example.administrator.myapplication.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager

import com.example.administrator.myapplication.R


/**
 * Created by gxj on 2017/9/4.
 * dialog 基类
 */

abstract class BaseDialog : Dialog {
     constructor(context: Context, themeResId: Int) : super(context, themeResId)

    protected abstract val layoutId: Int

    /**
     * 获取 dialog 显示在界面的位置样式
     *
     * @return 位置样式
     */
    protected open var gravity: Int = 0
        get() = Gravity.BOTTOM

    /**
     * 获取 dialog 显示在界面的动画样式
     *
     * @return 位置样式
     */
    protected open var animations: Int = 0
        get() = R.style.DataSheetAnimation

    /**
     * 子类复写 去实现 dialog 宽度的选择
     *
     * @return dialog宽度
     */
    protected open var widthStyle: Int = 0
        get() = WindowManager.LayoutParams.MATCH_PARENT

    constructor(context: Context) : this(context, R.style.BottonDialogTheme) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowsStyle()
        setContentView(layoutId)
        initView()
        initListener()
    }

    /**
     * 初始化View布局
     */
    protected abstract fun initView()

    /**
     * 初始化控件监听事件
     */
    protected abstract fun initListener()

    private fun setWindowsStyle() {
        //去掉dialog的标题
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        /* 设置与屏幕等宽 */
        val window = window

        /*
         * dialog 默认的样式@android:style/Theme.Dialog 对应的style 有pading属性 就能够水平占满
         */
        window?.decorView?.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = widthStyle
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = gravity
        window.attributes = lp
        window.setWindowAnimations(animations)
    }

}

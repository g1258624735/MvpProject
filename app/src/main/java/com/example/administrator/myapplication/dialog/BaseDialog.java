package com.example.administrator.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.myapplication.R;


/**
 * Created by gxj on 2017/9/4.
 * dialog 基类
 */

public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        this(context, R.style.BottonDialogTheme);
    }

    private BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowsStyle();
        setContentView(getLayoutId());
        initView();
        initListener();
    }

    protected abstract int getLayoutId();

    /**
     * 初始化View布局
     */
    protected abstract void initView();

    /**
     * 初始化控件监听事件
     */
    protected abstract void initListener();

    protected void setWindowsStyle() {
        //去掉dialog的标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /* 设置与屏幕等宽 */
        Window window = getWindow();

        /*
         * dialog 默认的样式@android:style/Theme.Dialog 对应的style 有pading属性 就能够水平占满
         */
        assert window != null;
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = getWidthStyle();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = getGravity();
        window.setAttributes(lp);
        window.setWindowAnimations(getAnimations());
    }

    /**
     * 获取 dialog 显示在界面的位置样式
     *
     * @return 位置样式
     */
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    /**
     * 获取 dialog 显示在界面的动画样式
     *
     * @return 位置样式
     */
    protected int getAnimations() {
        return R.style.DataSheetAnimation;
    }

    /**
     * 子类复写 去实现 dialog 宽度的选择
     *
     * @return dialog宽度
     */
    protected int getWidthStyle() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

}

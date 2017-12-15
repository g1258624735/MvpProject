package com.example.administrator.myapplication.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by gxj on 2017/9/4.
 * 通用dialog
 * 三种通用dialog 显示样式
 * 1-> 只显示文本
 * 2-> 显示输入框
 * 3-> 显示列表样式
 */
public class CommonDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_content_desc;
    private LinearLayout linear_bottom;
    private TextView tv_cancel;
    private TextView tv_sure;
    private TextView tv_one_sure;
    private EditText et_content;
    private Context context;
    private String leftText;
    private String rightText;
    private String content;
    private String title;
    private String etHint;
    private String desc;
    private int gravity = DialogGravityType.GRAVITY_CENTER;
    private int animationStyle = DialogAnimationType.DIALOG_ANIMATION_SCALE;

    /**
     * 位置显示样式选择
     */
    public interface DialogGravityType {
        /**
         * dialog 位置显示样式
         * 底部显示
         */
        int GRAVITY_BOTTOM = Gravity.BOTTOM;
        /**
         * dialog 中部显示
         */
        int GRAVITY_CENTER = Gravity.CENTER;
        /**
         * dialog 顶部显示
         */
        int GRAVITY_TOP = Gravity.TOP;
    }

    /**
     * 位置显示样式选择
     */
    public interface DialogAnimationType {
        /**
         * dialog 弹出动画
         * 渐变显示
         */
        int DIALOG_ANIMATION_SCALE = R.style.DialogScaleAnimation;
        /**
         * dialog 弹出动画
         * 从底部弹出 动画
         */
        int DIALOG_ANIMATION_UP_DOWN = R.style.DataSheetAnimation;
    }

    /**
     * 是否显示一个button
     * 1 -> 只有一个确定按钮
     * 2-> 显示确定和取消按钮
     */
    private boolean isShowOneButton = false;
    /**
     * 是否为输入框模式
     * true -> 输入框模式
     * false -> 文本模式
     */
    private boolean isShowEditText = false;
    private OnCommonClickListener leftOnClickListener;
    private OnCommonClickListener rightOnClickListener;
    private OnCommonClickListener oneButtonSureOnClickListener;

    public CommonDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getAnimations() {
        return animationStyle;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_common_layout;
    }

    @Override
    protected int getGravity() {
        return gravity;
    }

    @Override
    protected int getWidthStyle() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_content_desc = findViewById(R.id.tv_content_desc);
        linear_bottom = findViewById(R.id.ll_container);
        linear_bottom = findViewById(R.id.ll_container);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_sure = findViewById(R.id.tv_sure);
        tv_one_sure = findViewById(R.id.tv_one_sure);
        et_content = findViewById(R.id.et_content);
        initUI();
    }

    private void initUI() {
        linear_bottom.setVisibility(isShowOneButton ? View.GONE : View.VISIBLE);
        tv_one_sure.setVisibility(isShowOneButton ? View.VISIBLE : View.GONE);
        tv_content.setVisibility(isShowEditText ? View.GONE : View.VISIBLE);
        et_content.setVisibility(isShowEditText ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            tv_content.setText(content);
        }
        if (!TextUtils.isEmpty(desc)) {
            tv_content_desc.setText(desc);
        }
        if (!TextUtils.isEmpty(leftText)) {
            tv_cancel.setText(leftText);
        }
        if (!TextUtils.isEmpty(rightText)) {
            tv_sure.setText(rightText);
        }
        if (!TextUtils.isEmpty(rightText)) {
            tv_one_sure.setText(rightText);
        }
        if (!TextUtils.isEmpty(etHint)) {
            et_content.setHint(etHint);
        }
    }

    @Override
    protected void initListener() {
        tv_cancel.setOnClickListener(this);
        tv_one_sure.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel://取消
                dismiss();
                if (leftOnClickListener != null) {
                    leftOnClickListener.onClick(this, et_content.getText().toString().trim());
                }
                break;
            case R.id.tv_sure://确定
                if (rightOnClickListener != null) {
                    rightOnClickListener.onClick(this, et_content.getText().toString().trim());
                }
                break;
            case R.id.tv_one_sure://只有一个确定按钮样式
                if (oneButtonSureOnClickListener != null) {
                    oneButtonSureOnClickListener.onClick(this, et_content.getText().toString().trim());
                }
                break;
        }
    }

    public interface OnCommonClickListener {
        void onClick(CommonDialog dialog, String text);
    }

    /**
     * dialog 属性构建者
     */
    public static class Builder {
        private final CommonDialog dialog;

        public Builder(Context context) {
            dialog = new CommonDialog(context);
        }

        /**
         * 取消按钮文字
         */
        public Builder setLeftText(String leftText) {
            dialog.leftText = leftText;
            return this;
        }

        /**
         * 确认按钮文字
         */
        public Builder setRightText(String rightText) {
            dialog.rightText = rightText;
            return this;
        }

        /**
         * 正文
         */
        public Builder setContent(String content) {
            dialog.content = content;
            return this;
        }

        /**
         * 描述文字
         */
        public Builder setDes(String desc) {
            dialog.desc = desc;
            return this;
        }

        /**
         * 是否只显示一个确定 按钮
         */
        public Builder setEnableOneButton(boolean isOneButton) {
            dialog.isShowOneButton = isOneButton;
            return this;
        }

        /**
         * 设置dialog 显示屏幕中的位置
         */
        public Builder setGravity(int gravity) {
            dialog.gravity = gravity;
            return this;
        }

        /**
         * 设置dialog 弹出动画
         */
        public Builder setAnimationStyle(int animationStyle) {
            dialog.animationStyle = animationStyle;
            return this;
        }

        /**
         * 是否为输入框模式
         * 输入框模式下会隐藏内容文本
         */
        public Builder setShowEditText(boolean isShowEditText) {
            dialog.isShowEditText = isShowEditText;
            return this;
        }

        /**
         * 标题
         */
        public Builder setTitle(String title) {
            dialog.title = title;
            return this;
        }

        /**
         * 输入框提示语
         */
        public Builder setEditTextHint(String hint) {
            dialog.etHint = hint;
            return this;
        }

        /**
         * 左按钮点击事件
         */
        public Builder setLeftOnClickListener(OnCommonClickListener leftOnClickListener) {
            dialog.leftOnClickListener = leftOnClickListener;
            return this;
        }

        /**
         * 右按钮点击事件
         */
        public Builder setRightOnClickListener(OnCommonClickListener rightOnClickListener) {
            dialog.rightOnClickListener = rightOnClickListener;
            return this;
        }

        /**
         * 只有一个确定按钮的点击事件
         */
        public Builder setOneButtonSureOnClickListener(OnCommonClickListener oneButtonSureOnClickListener) {
            dialog.oneButtonSureOnClickListener = oneButtonSureOnClickListener;
            return this;
        }


        /**
         * 返回dialog
         *
         * @return CommonDialog
         */
        public CommonDialog create() {
            return dialog;
        }

    }
}

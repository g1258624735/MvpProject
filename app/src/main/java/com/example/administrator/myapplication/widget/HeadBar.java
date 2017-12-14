package com.example.administrator.myapplication.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;


public class HeadBar extends RelativeLayout {

    private Button btnIncludeTitleBack;
    private TextView tvIncludeTitle;
    private ImageView ivIncludeTitleRight;
    public TextView btnIncludeTitleRight;

    private String mleft;
    private String mTitle;
    private String mRight;
    private boolean isRightIvGone;
    private boolean isBackIconGone;
    private int imgRightRes;
    private int leftImgRes;
    private int rightBtnImgRes;
    private int rightTextSize;
    private int leftTextSize;
    private int rightTextColor;
    private int leftTextColor;

    public HeadBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HeadBar);
        mTitle = ta.getString(R.styleable.HeadBar_titleText);
        mleft = ta.getString(R.styleable.HeadBar_leftText);
        mRight = ta.getString(R.styleable.HeadBar_rightText);
        isBackIconGone = ta.getBoolean(R.styleable.HeadBar_isBackIconGone, false);
        isRightIvGone = !TextUtils.isEmpty(mRight);
        imgRightRes = ta.getResourceId(R.styleable.HeadBar_rightImgRes, -1);
        leftImgRes = ta.getResourceId(R.styleable.HeadBar_leftImgRes, -1);
        rightBtnImgRes = ta.getResourceId(R.styleable.HeadBar_rightBtnImgRes, -1);

        rightTextSize = ta.getDimensionPixelSize(R.styleable.HeadBar_rightTextSize, sp2px(getContext(), 14));
        leftTextSize = ta.getDimensionPixelSize(R.styleable.HeadBar_leftTextSize, sp2px(getContext(), 14));
        rightTextColor = ta.getColor(R.styleable.HeadBar_rightTextColor, Color.BLACK);
        leftTextColor = ta.getColor(R.styleable.HeadBar_leftTextColor, Color.BLACK);
        ta.recycle();

        btnIncludeTitleBack.setTextColor(leftTextColor);
        btnIncludeTitleBack.setTextSize(px2sp(getContext(), leftTextSize));
        btnIncludeTitleRight.setTextColor(rightTextColor);
        btnIncludeTitleRight.setTextSize(px2sp(getContext(), rightTextSize));
        if (isBackIconGone) {
            btnIncludeTitleBack.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        if (!TextUtils.isEmpty(mleft)) {
            btnIncludeTitleBack.setText(mleft);
        }
        if (!TextUtils.isEmpty(mTitle)) {
            tvIncludeTitle.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mRight)) {
            btnIncludeTitleRight.setText(mRight);
        }
        btnIncludeTitleRight.setVisibility(isRightIvGone ? VISIBLE : GONE);
        ivIncludeTitleRight.setVisibility(isRightIvGone ? GONE : VISIBLE);
        if (imgRightRes != -1) {
            ivIncludeTitleRight.setImageResource(imgRightRes);
        }
        if (leftImgRes != -1) {
            btnIncludeTitleBack.setCompoundDrawablesWithIntrinsicBounds(leftImgRes, 0, 0, 0);
        }
        if (rightBtnImgRes != -1) {
            btnIncludeTitleRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightBtnImgRes, 0);
        }

    }

    public HeadBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.head_bar, this, true);
        btnIncludeTitleBack = findViewById(R.id.btn_include_title_back);
        btnIncludeTitleRight = findViewById(R.id.btn_include_title_right);
        tvIncludeTitle = findViewById(R.id.tv_include_title);
        ivIncludeTitleRight = findViewById(R.id.iv_include_title_right);
    }

    public void SetIvRightOnclickerListener(OnClickListener listener) {
        if (ivIncludeTitleRight.getVisibility() == VISIBLE)
            ivIncludeTitleRight.setOnClickListener(listener);
    }

    public void SetBtnRightOnclickerListener(OnClickListener listener) {
        if (btnIncludeTitleRight.getVisibility() == VISIBLE)
            btnIncludeTitleRight.setOnClickListener(listener);
    }

    /**
     * 此方法一般在fragment中使用时调用，同时将所在activity的backclick方法重写屏蔽点击事件；
     * activity如果需要修改该按钮的点击事件，请重写baseactivity的backclick方法
     *
     * @param listener
     */
    public void SetBtnLeftOnclickerListener(OnClickListener listener) {
        if (btnIncludeTitleBack.getVisibility() == VISIBLE)
            btnIncludeTitleBack.setOnClickListener(listener);
    }

    public void SetTitleOnclickerListener(OnClickListener listener) {
        if (tvIncludeTitle.getVisibility() == VISIBLE)
            tvIncludeTitle.setOnClickListener(listener);
    }

    public void setRightText(CharSequence str) {
        if (!TextUtils.isEmpty(str)) {
            btnIncludeTitleRight.setText(str);
        }
    }

    public void setTitleText(CharSequence str) {
        if (!TextUtils.isEmpty(str)) {
            tvIncludeTitle.setText(str);
        }
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 设置右边按钮能否点击状态
     */
    public void setBtnIncludeTitleRightEnable(boolean isEnable) {
        btnIncludeTitleRight.setEnabled(isEnable);
        btnIncludeTitleRight.setTextColor(isEnable ? Color.parseColor("#323232") : Color.parseColor("#989898"));
    }

    public void setBtnIncludeTitleRightVisible(boolean isVisible) {
        btnIncludeTitleRight.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setBtnIncludeTitleBackVisible(boolean isVisible) {
        btnIncludeTitleBack.setVisibility(isVisible ? VISIBLE : GONE);
    }


}

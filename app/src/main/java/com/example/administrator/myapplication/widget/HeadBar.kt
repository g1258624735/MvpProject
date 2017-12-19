package com.example.administrator.myapplication.widget


import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.administrator.myapplication.R

/**
 * 通用导航栏
 */

class HeadBar : RelativeLayout {

    private var btnIncludeTitleBack: Button? = null
    private var tvIncludeTitle: TextView? = null
    private var ivIncludeTitleRight: ImageView? = null
    private var btnIncludeTitleRight: TextView ?=null

    private var mleft: String?=""
    private var mTitle: String?=""
    private var mRight: String?=""
    private var isRightIvGone: Boolean = false
    private var isBackIconGone: Boolean = false
    private var imgRightRes: Int = 0
    private var leftImgRes: Int = 0
    private var rightBtnImgRes: Int = 0
    private var rightTextSize: Int = 0
    private var leftTextSize: Int = 0
    private var rightTextColor: Int = 0
    private var leftTextColor: Int = 0
    private var titleTextColor: Int = 0

    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {
        init(context)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HeadBar)
        mTitle = ta.getString(R.styleable.HeadBar_titleText)
        mleft = ta.getString(R.styleable.HeadBar_leftText)
        mRight = ta.getString(R.styleable.HeadBar_rightText)
        isBackIconGone = ta.getBoolean(R.styleable.HeadBar_isBackIconGone, false)
        isRightIvGone = !TextUtils.isEmpty(mRight)
        imgRightRes = ta.getResourceId(R.styleable.HeadBar_rightImgRes, -1)
        leftImgRes = ta.getResourceId(R.styleable.HeadBar_leftImgRes, -1)
        rightBtnImgRes = ta.getResourceId(R.styleable.HeadBar_rightBtnImgRes, -1)

        rightTextSize = ta.getDimensionPixelSize(R.styleable.HeadBar_rightTextSize, sp2px(getContext(), 14f))
        leftTextSize = ta.getDimensionPixelSize(R.styleable.HeadBar_leftTextSize, sp2px(getContext(), 14f))
        rightTextColor = ta.getColor(R.styleable.HeadBar_rightTextColor, Color.BLACK)
        leftTextColor = ta.getColor(R.styleable.HeadBar_leftTextColor, Color.BLACK)
        titleTextColor = ta.getColor(R.styleable.HeadBar_titleTextColor, Color.BLACK)
        ta.recycle()

        btnIncludeTitleBack!!.setTextColor(leftTextColor)
        btnIncludeTitleBack!!.textSize = px2sp(getContext(), leftTextSize.toFloat()).toFloat()
        btnIncludeTitleRight!!.setTextColor(rightTextColor)
        btnIncludeTitleRight!!.textSize = px2sp(getContext(), rightTextSize.toFloat()).toFloat()
        tvIncludeTitle!!.setTextColor(titleTextColor)
        if (isBackIconGone) {
            btnIncludeTitleBack!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        if (!TextUtils.isEmpty(mleft)) {
            btnIncludeTitleBack!!.text = mleft
        }
        if (!TextUtils.isEmpty(mTitle)) {
            tvIncludeTitle!!.text = mTitle
        }
        if (!TextUtils.isEmpty(mRight)) {
            btnIncludeTitleRight!!.text = mRight
        }
        btnIncludeTitleRight!!.visibility = if (isRightIvGone) View.VISIBLE else View.GONE
        ivIncludeTitleRight!!.visibility = if (isRightIvGone) View.GONE else View.VISIBLE
        if (imgRightRes != -1) {
            ivIncludeTitleRight!!.setImageResource(imgRightRes)
        }
        if (leftImgRes != -1) {
            btnIncludeTitleBack!!.setCompoundDrawablesWithIntrinsicBounds(leftImgRes, 0, 0, 0)
        }
        if (rightBtnImgRes != -1) {
            btnIncludeTitleRight!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightBtnImgRes, 0)
        }

    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.head_bar, this, true)
        btnIncludeTitleBack = findViewById(R.id.btn_include_title_back)
        btnIncludeTitleRight = findViewById(R.id.btn_include_title_right)
        tvIncludeTitle = findViewById(R.id.tv_include_title)
        ivIncludeTitleRight = findViewById(R.id.iv_include_title_right)
    }

    fun SetIvRightOnclickerListener(listener: View.OnClickListener) {
        if (ivIncludeTitleRight!!.visibility == View.VISIBLE)
            ivIncludeTitleRight!!.setOnClickListener(listener)
    }

    fun SetBtnRightOnclickerListener(listener: View.OnClickListener) {
        if (btnIncludeTitleRight!!.visibility == View.VISIBLE)
            btnIncludeTitleRight!!.setOnClickListener(listener)
    }

    /**
     * 此方法一般在fragment中使用时调用，同时将所在activity的backclick方法重写屏蔽点击事件；
     * activity如果需要修改该按钮的点击事件，请重写baseactivity的backclick方法
     *
     * @param listener
     */
    fun SetBtnLeftOnclickerListener(listener: View.OnClickListener) {
        if (btnIncludeTitleBack!!.visibility == View.VISIBLE)
            btnIncludeTitleBack!!.setOnClickListener(listener)
    }

    fun SetTitleOnclickerListener(listener: View.OnClickListener) {
        if (tvIncludeTitle!!.visibility == View.VISIBLE)
            tvIncludeTitle!!.setOnClickListener(listener)
    }

    fun setRightText(str: CharSequence) {
        if (!TextUtils.isEmpty(str)) {
            btnIncludeTitleRight!!.text = str
        }
    }

    fun setTitleText(str: CharSequence) {
        if (!TextUtils.isEmpty(str)) {
            tvIncludeTitle!!.text = str
        }
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    private fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 设置右边按钮能否点击状态
     */
    fun setBtnIncludeTitleRightEnable(isEnable: Boolean) {
        btnIncludeTitleRight!!.isEnabled = isEnable
        btnIncludeTitleRight!!.setTextColor(if (isEnable) Color.parseColor("#323232") else Color.parseColor("#989898"))
    }

    fun setBtnIncludeTitleRightVisible(isVisible: Boolean) {
        btnIncludeTitleRight!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setBtnIncludeTitleBackVisible(isVisible: Boolean) {
        btnIncludeTitleBack!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


}

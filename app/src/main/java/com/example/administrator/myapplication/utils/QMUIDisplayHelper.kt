package com.example.administrator.myapplication.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.ViewConfiguration
import android.view.WindowManager

import java.lang.reflect.Field
import java.util.Locale

/**
 * @author cginechen
 * @date 2016-03-17
 */
object QMUIDisplayHelper {

    /**
     * 屏幕密度,系统源码注释不推荐使用
     */
    val DENSITY = Resources.getSystem()
            .displayMetrics.density
    private val TAG = "Devices"
    /**
     * 屏幕密度
     */
    var sDensity = 0f
    /**
     * 是否有摄像头
     */
    private var sHasCamera: Boolean? = null

    /**
     * 判断 SD Card 是否 ready
     */
    val isSdcardReady: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState()


    val isElevationSupported: Boolean
        get() = Build.VERSION.SDK_INT >= 21

    /**
     * 获取 DisplayMetrics
     */
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        (context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    /**
     * 把以 dp 为单位的值，转化为以 px 为单位的值
     *
     * @param dpValue 以 dp 为单位的值
     * @return px value
     */
    fun dpToPx(dpValue: Int): Int {
        return (dpValue * DENSITY + 0.5f).toInt()
    }

    /**
     * 把以 px 为单位的值，转化为以 dp 为单位的值
     *
     * @param pxValue 以 px 为单位的值
     * @return dp值
     */
    fun pxToDp(pxValue: Float): Int {
        return (pxValue / DENSITY + 0.5f).toInt()
    }

    fun getDensity(context: Context): Float {
        if (sDensity == 0f) {
            sDensity = getDisplayMetrics(context).density
        }
        return sDensity
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }

    /**
     * 获取屏幕的真实宽高
     */
    fun getRealScreenSize(context: Context): IntArray {
        val size = IntArray(2)
        var widthPixels: Int
        var heightPixels: Int
        val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels
        heightPixels = metrics.heightPixels
        try {
            // used when 17 > SDK_INT >= 14; includes window decorations (statusbar bar/menu bar)
            widthPixels = Display::class.java!!.getMethod("getRawWidth").invoke(d) as Int
            heightPixels = Display::class.java!!.getMethod("getRawHeight").invoke(d) as Int
        } catch (ignored: Exception) {
        }

        try {
            // used when SDK_INT >= 17; includes window decorations (statusbar bar/menu bar)
            val realSize = Point()
            Display::class.java!!.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
            widthPixels = realSize.x
            heightPixels = realSize.y
        } catch (ignored: Exception) {
        }

        size[0] = widthPixels
        size[1] = heightPixels
        return size

    }

    /**
     * 单位转换: dp -> px
     */
    fun dp2px(context: Context, dp: Int): Int {
        return (getDensity(context) * dp + 0.5).toInt()
    }

    /**
     * 单位转换:px -> dp
     */
    fun px2dp(context: Context, px: Int): Int {
        return (px / getDensity(context) + 0.5).toInt()
    }

    /**
     * 判断是否有状态栏
     */
    fun hasStatusBar(context: Context): Boolean {
        if (context is Activity) {
            val attrs = context.window.attributes
            return attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != WindowManager.LayoutParams.FLAG_FULLSCREEN
        }
        return true
    }

    /**
     * 获取ActionBar高度
     */
    fun getActionBarHeight(context: Context): Int {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.resources.displayMetrics)
        }
        return actionBarHeight
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val c: Class<*>
        val obj: Any
        val field: Field
        val x: Int
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field.get(obj).toString())
            return context.resources
                    .getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    /**
     * 获取虚拟菜单的高度,若无则返回0
     */
    fun getNavMenuHeight(context: Context): Int {
        return getRealScreenSize(context)[1] - getScreenHeight(context)
    }

    fun hasCamera(context: Context): Boolean {
        if (sHasCamera == null) {
            val pckMgr = context.packageManager
            val flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front")
            val flag1 = pckMgr.hasSystemFeature("android.hardware.camera")
            val flag2: Boolean
            flag2 = flag || flag1
            sHasCamera = flag2
        }
        return sHasCamera!!
    }

    /**
     * 是否有硬件menu
     */
    fun hasHardwareMenuKey(context: Context): Boolean {
        val flag: Boolean
        if (Build.VERSION.SDK_INT < 11)
            flag = true
        else if (Build.VERSION.SDK_INT >= 14) {
            flag = ViewConfiguration.get(context).hasPermanentMenuKey()
        } else
            flag = false
        return flag
    }

    /**
     * 是否有网络功能
     */
    fun hasInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    /**
     * 判断是否存在pckName包
     */
    fun isPackageExist(context: Context, pckName: String): Boolean {
        try {
            val pckInfo = context.packageManager
                    .getPackageInfo(pckName, 0)
            if (pckInfo != null)
                return true
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        return false
    }

    /**
     * 获取当前国家的语言
     */
    fun getCurCountryLan(context: Context): String {
        val config = context.resources.configuration
        val sysLocale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.locales.get(0)
        } else {

            sysLocale = config.locale
        }
        return (sysLocale.language
                + "-"
                + sysLocale.country)
    }

    /**
     * 判断是否为中文环境
     */
    fun isZhCN(context: Context): Boolean {
        val config = context.resources.configuration
        val sysLocale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.locales.get(0)
        } else {

            sysLocale = config.locale
        }
        val lang = sysLocale.country
        return lang.equals("CN", ignoreCase = true)
    }

    /**
     * 设置全屏
     */
    fun setFullScreen(context: Context) {
        if (context is Activity) {
            val params = context.window.attributes
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            context.window.attributes = params
            context.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

    }

    /**
     * 取消全屏
     */
    fun cancelFullScreen(context: Context) {
        if (context is Activity) {
            val params = context.window.attributes
            params.flags = params.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            context.window.attributes = params
            context.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    /**
     * 判断是否全屏
     */
    fun isFullScreen(activity: Activity): Boolean {
        val params = activity.window.attributes
        return params.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
    }
}

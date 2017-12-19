package com.example.administrator.myapplication.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.IntDef
import android.support.annotation.RequiresApi
import android.view.View
import android.view.Window
import android.view.WindowManager

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * @author cginechen
 * @date 2016-03-27
 */
object QMUIStatusBarHelper {

    private const val STATUSBAR_TYPE_DEFAULT = 0
    private const val STATUSBAR_TYPE_MIUI = 1
    private const val STATUSBAR_TYPE_FLYME = 2
    private const val STATUSBAR_TYPE_ANDROID6 = 3 // Android 6.0
    private const val STATUS_BAR_DEFAULT_HEIGHT_DP = 25 // 大部分状态栏都是25dp
    // 在某些机子上存在不同的density值，所以增加两个虚拟值
    var sVirtualDensity = -1f
    var sVirtualDensityDpi = -1f
    private var sStatusbarHeight = -1
    @StatusBarType private var mStatuBarType = STATUSBAR_TYPE_DEFAULT
    private var sTransparentValue: Int? = null

    /**
     * 更改状态栏图标、文字颜色的方案是否是MIUI自家的， MIUI9之后用回Android原生实现
     * 见小米开发文档说明：https://dev.mi.com/console/doc/detail?pId=1159
     */
    private val isMIUICustomStatusBarLightModeImpl: Boolean
        get() = QMUIDeviceHelper.isMIUIV5 || QMUIDeviceHelper.isMIUIV6 ||
                QMUIDeviceHelper.isMIUIV7 || QMUIDeviceHelper.isMIUIV8

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun translucent(activity: Activity) {
        translucent(activity, 0x40000000)
    }

    /**
     * 沉浸式状态栏。
     * 支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android。
     *
     * @param activity 需要被设置沉浸式状态栏的 Activity。
     */
    @TargetApi(19)
    fun translucent(activity: Activity, @ColorInt colorOn5x: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // 版本小于4.4，绝对不考虑沉浸式
            return
        }
        // 小米和魅族4.4 以上版本支持沉浸式
        if (QMUIDeviceHelper.isMeizu || QMUIDeviceHelper.isMIUI) {
            val window = activity.window
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else {
                // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何？
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
                //                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = colorOn5x
            }
            //        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //            // android4.4的默认是从上到下黑到透明，我们的背景是白色，很难看，因此只做魅族和小米的
            //        } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1){
            //            // 如果app 为白色，需要更改状态栏颜色，因此不能让19一下支持透明状态栏
            //            Window window = activity.getWindow();
            //            Integer transparentValue = getStatusBarAPITransparentValue(activity);
            //            if(transparentValue != null) {
            //                window.getDecorView().setSystemUiVisibility(transparentValue);
            //            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
            /**
             * 设置状态栏黑色字体图标，
             * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
             *
             * @param activity 需要被处理的 Activity
             */
    fun setStatusBarLightMode(activity: Activity): Boolean {
        // 无语系列：ZTK C2016只能时间和电池图标变色。。。。
        if (QMUIDeviceHelper.isZTKC2016) {
            return false
        }

        if (mStatuBarType != STATUSBAR_TYPE_DEFAULT) {
            return setStatusBarLightMode(activity, mStatuBarType)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMIUICustomStatusBarLightModeImpl && MIUISetStatusBarLightMode(activity.window, true)) {
                mStatuBarType = STATUSBAR_TYPE_MIUI
                return true
            } else if (FlymeSetStatusBarLightMode(activity.window, true)) {
                mStatuBarType = STATUSBAR_TYPE_FLYME
                return true
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Android6SetStatusBarLightMode(activity.window, true)
                mStatuBarType = STATUSBAR_TYPE_ANDROID6
                return true
            }
        }
        return false
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     * @param type     StatusBar 类型，对应不同的系统
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setStatusBarLightMode(activity: Activity, @StatusBarType type: Int): Boolean {
        if (type == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.window, true)
        } else if (type == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.window, true)
        } else if (type == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.window, true)
        }
        return false
    }


    @RequiresApi(Build.VERSION_CODES.M)
            /**
             * 设置状态栏白色字体图标
             * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
             */
    fun setStatusBarDarkMode(activity: Activity): Boolean {
        if (mStatuBarType == STATUSBAR_TYPE_DEFAULT) {
            // 默认状态，不需要处理
            return true
        }

        if (mStatuBarType == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.window, false)
        } else if (mStatuBarType == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.window, false)
        } else if (mStatuBarType == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.window, false)
        }
        return true
    }

    @TargetApi(23)
    private fun changeStatusBarModeRetainFlag(window: Window, out: Int): Int {
        var out = out
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        return out
    }

    fun retainSystemUiFlag(window: Window, out: Int, type: Int): Int {
        var out = out
        val now = window.decorView.systemUiVisibility
        if (now and type == type) {
            out = out or type
        }
        return out
    }


    /**
     * 设置状态栏字体图标为深色，Android 6
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @TargetApi(23)
    private fun Android6SetStatusBarLightMode(window: Window?, light: Boolean): Boolean {
        val decorView = window!!.decorView
        var systemUi = if (light) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        systemUi = changeStatusBarModeRetainFlag(window, systemUi)
        decorView.systemUiVisibility = systemUi
        return true
    }

    /**
     * 设置状态栏字体图标为深色，需要 MIUIV6 以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回 true
     */
    fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true
            } catch (ignored: Exception) {

            }

        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
            /**
             * 设置状态栏图标为深色和魅族特定的文字风格
             * 可以用来判断是否为 Flyme 用户
             *
             * @param window 需要设置的窗口
             * @param dark   是否把状态栏字体及图标颜色设置为深色
             * @return boolean 成功执行返回true
             */
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {

        // flyme 在 6.2.0.0A 支持了 Android 官方的实现方案，旧的方案失效
        Android6SetStatusBarLightMode(window, dark)

        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java!!
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java!!
                        .getDeclaredField("meizuFlags")
                darkFlag.setAccessible(true)
                meizuFlags.setAccessible(true)
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (ignored: Exception) {

            }

        }
        return result
    }

    /**
     * 获取是否全屏
     *
     * @return 是否全屏
     */
    fun isFullScreen(activity: Activity): Boolean {
        var ret = false
        try {
            val attrs = activity.window.attributes
            ret = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != 0
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    /**
     * API19之前透明状态栏：获取设置透明状态栏的system ui visibility的值，这是部分有提供接口的rom使用的
     * http://stackoverflow.com/questions/21865621/transparent-status-bar-before-4-4-kitkat
     */
    fun getStatusBarAPITransparentValue(context: Context): Int? {
        if (sTransparentValue != null) {
            return sTransparentValue
        }
        val systemSharedLibraryNames = context.packageManager
                .systemSharedLibraryNames
        var fieldName: String? = null
        for (lib in systemSharedLibraryNames) {
            if ("touchwiz" == lib) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT_BACKGROUND"
            } else if (lib.startsWith("com.sonyericsson.navigationbar")) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT"
            }
        }

        if (fieldName != null) {
            try {
                val field = View::class.java!!.getField(fieldName)
                if (field != null) {
                    val type = field!!.getType()
                    if (type == Int::class.javaPrimitiveType) {
                        sTransparentValue = field!!.getInt(null)
                    }
                }
            } catch (ignored: Exception) {
            }

        }
        return sTransparentValue
    }

    /**
     * 检测 Android 6.0 是否可以启用 window.setStatusBarColor(Color.TRANSPARENT)。
     */
    fun supportTransclentStatusBar6(): Boolean {
        return !(QMUIDeviceHelper.isZUKZ1 || QMUIDeviceHelper.isZTKC2016)
    }

    /**
     * 获取状态栏的高度。
     */
    fun getStatusbarHeight(context: Context): Int {
        if (sStatusbarHeight == -1) {
            initStatusBarHeight(context)
        }
        return sStatusbarHeight
    }

    private fun initStatusBarHeight(context: Context) {
        val clazz: Class<*>
        var obj: Any? = null
        var field: Field? = null
        try {
            clazz = Class.forName("com.android.internal.R\$dimen")
            obj = clazz.newInstance()
            if (QMUIDeviceHelper.isMeizu) {
                try {
                    field = clazz.getField("status_bar_height_large")
                } catch (t: Throwable) {
                    t.printStackTrace()
                }

            }
            if (field == null) {
                field = clazz.getField("status_bar_height")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        if (field != null && obj != null) {
            try {
                val id = Integer.parseInt(field.get(obj).toString())
                sStatusbarHeight = context.resources.getDimensionPixelSize(id)
            } catch (t: Throwable) {
                t.printStackTrace()
            }

        }
        if (QMUIDeviceHelper.isTablet(context) && sStatusbarHeight > QMUIDisplayHelper.dp2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP)) {
            //状态栏高度大于25dp的平板，状态栏通常在下方
            sStatusbarHeight = 0
        } else {
            if (sStatusbarHeight <= 0 || sStatusbarHeight > QMUIDisplayHelper.dp2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP * 2)) {
                //安卓默认状态栏高度为25dp，如果获取的状态高度大于2倍25dp的话，这个数值可能有问题，用回桌面定义的值从新获取。出现这种可能性较低，只有小部分手机出现
                if (sVirtualDensity == -1f) {
                    sStatusbarHeight = QMUIDisplayHelper.dp2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP)
                } else {
                    sStatusbarHeight = (STATUS_BAR_DEFAULT_HEIGHT_DP * sVirtualDensity + 0.5f).toInt()
                }
            }
        }
    }

    fun setVirtualDensity(density: Float) {
        sVirtualDensity = density
    }

    fun setVirtualDensityDpi(densityDpi: Float) {
        sVirtualDensityDpi = densityDpi
    }

    @IntDef(STATUSBAR_TYPE_DEFAULT.toLong(), STATUSBAR_TYPE_MIUI.toLong(), STATUSBAR_TYPE_FLYME.toLong(), STATUSBAR_TYPE_ANDROID6.toLong())
    @Retention(RetentionPolicy.SOURCE)
    private annotation class StatusBarType

}

package com.example.administrator.myapplication.inteface

import android.graphics.Rect
import android.support.v4.view.WindowInsetsCompat

/**
 * @author cginechen
 * @date 2017-09-13
 */

interface IWindowInsetLayout {
    fun applySystemWindowInsets19(insets: Rect): Boolean

    fun applySystemWindowInsets21(insets: WindowInsetsCompat): Boolean
}

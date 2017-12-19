package com.example.administrator.myapplication.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Rect
import android.os.Build
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.WindowInsetsCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


import com.example.administrator.myapplication.inteface.IWindowInsetLayout

import java.lang.ref.WeakReference

/**
 * @author cginechen
 * @date 2017-09-13
 */

class QMUIWindowInsetHelper(viewGroup: ViewGroup, windowInsetLayout: IWindowInsetLayout) {
    private val KEYBOARD_HEIGHT_BOUNDARY: Int
    private val mWindowInsetLayoutWR: WeakReference<IWindowInsetLayout>

    init {
        mWindowInsetLayoutWR = WeakReference(windowInsetLayout)
        KEYBOARD_HEIGHT_BOUNDARY = QMUIDisplayHelper.dp2px(viewGroup.context, 100)
        ViewCompat.setOnApplyWindowInsetsListener(viewGroup
        ) { v, insets -> setWindowInsets(insets) }
    }

    private fun setWindowInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        if (Build.VERSION.SDK_INT >= 21 && mWindowInsetLayoutWR.get() != null) {
            if (mWindowInsetLayoutWR.get()!!.applySystemWindowInsets21(insets)) {
                return insets.consumeSystemWindowInsets()
            }
        }
        return insets
    }

    @TargetApi(19)
    fun defaultApplySystemWindowInsets19(viewGroup: ViewGroup, insets: Rect): Boolean {
        var consumed = false
        if (insets.bottom >= KEYBOARD_HEIGHT_BOUNDARY) {
            QMUIViewHelper.setPaddingBottom(viewGroup, insets.bottom)
            insets.bottom = 0
        } else {
            QMUIViewHelper.setPaddingBottom(viewGroup, 0)
        }

        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (jumpDispatch(child)) {
                continue
            }

            val childInsets = Rect(insets)
            computeInsetsWithGravity(child, childInsets)

            if (!isHandleContainer(child)) {
                child.setPadding(childInsets.left, childInsets.top, childInsets.right, childInsets.bottom)
            } else {
                if (child is IWindowInsetLayout) {
                    (child as IWindowInsetLayout).applySystemWindowInsets19(childInsets)
                } else {
                    defaultApplySystemWindowInsets19(child as ViewGroup, childInsets)
                }
            }
            consumed = true
        }

        return consumed
    }

    @TargetApi(21)
    fun defaultApplySystemWindowInsets21(viewGroup: ViewGroup, insets: WindowInsetsCompat): Boolean {
        if (!insets.hasSystemWindowInsets()) {
            return false
        }
        var consumed = false
        var showKeyboard = false
        if (insets.systemWindowInsetBottom >= KEYBOARD_HEIGHT_BOUNDARY) {
            showKeyboard = true
            QMUIViewHelper.setPaddingBottom(viewGroup, insets.systemWindowInsetBottom)
        } else {
            QMUIViewHelper.setPaddingBottom(viewGroup, 0)
        }

        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)

            if (jumpDispatch(child)) {
                continue
            }

            val childInsets = Rect(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    if (showKeyboard) 0 else insets.systemWindowInsetBottom)

            computeInsetsWithGravity(child, childInsets)
            ViewCompat.dispatchApplyWindowInsets(child, insets.replaceSystemWindowInsets(childInsets))

            consumed = true
        }

        return consumed
    }

    @SuppressLint("RtlHardcoded")
    private fun computeInsetsWithGravity(view: View, insets: Rect) {
        val lp = view.layoutParams
        var gravity = -1
        if (lp is FrameLayout.LayoutParams) {
            gravity = lp.gravity
        }

        /**
         * 因为该方法执行时机早于 FrameLayout.layoutChildren，
         * 而在 {FrameLayout#layoutChildren} 中当 gravity == -1 时会设置默认值为 Gravity.TOP | Gravity.LEFT，
         * 所以这里也要同样设置
         */
        if (gravity == -1) {
            gravity = Gravity.TOP or Gravity.LEFT
        }

        if (lp.width != FrameLayout.LayoutParams.MATCH_PARENT) {
            val horizontalGravity = gravity and Gravity.HORIZONTAL_GRAVITY_MASK
            when (horizontalGravity) {
                Gravity.LEFT -> insets.right = 0
                Gravity.RIGHT -> insets.left = 0
            }
        }

        if (lp.height != FrameLayout.LayoutParams.MATCH_PARENT) {
            val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
            when (verticalGravity) {
                Gravity.TOP -> insets.bottom = 0
                Gravity.BOTTOM -> insets.top = 0
            }
        }
    }

    companion object {

        @TargetApi(19)
        fun jumpDispatch(child: View): Boolean {
            return !child.fitsSystemWindows && !isHandleContainer(child)
        }

        fun isHandleContainer(child: View): Boolean {
            return child is IWindowInsetLayout || child is CoordinatorLayout
        }
    }
}

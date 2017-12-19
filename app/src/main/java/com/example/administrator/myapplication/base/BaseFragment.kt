package com.example.administrator.myapplication.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout

import com.example.administrator.myapplication.R
import com.example.administrator.myapplication.utils.QMUIViewHelper
import com.example.administrator.myapplication.utils.QMUIWindowInsetLayout


/**
 * 基础 Fragment 类，提供各种基础功能。
 * Created by gxj on 15/9/14.
 */
abstract class BaseFragment : Fragment() {
    protected lateinit var activity: Activity
    private var mBaseView: View? = null

    val baseFragmentActivity: BaseFragmentActivity?
        get() = getActivity() as BaseFragmentActivity

    val isAttachedToActivity: Boolean
        get() = !isRemoving && mBaseView != null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activity = (context as Activity)
    }

    override fun onDetach() {
        super.onDetach()
        mBaseView = null
    }

    protected fun startFragment(fragment: BaseFragment) {
        val baseFragmentActivity = this.baseFragmentActivity
        if (baseFragmentActivity != null) {
            if (this.isAttachedToActivity) {
                baseFragmentActivity.startFragment(fragment)
            } else {
                Log.e("BaseFragment", "fragment not attached:" + this)
            }
        } else {
            Log.e("BaseFragment", "startFragment null:" + this)
        }
    }

    /**
     * 显示键盘
     */
    protected fun showKeyBoard() {
        val imm = getActivity().applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.toggleSoftInput(0, InputMethod.SHOW_FORCED)
    }

    /**
     * 隐藏键盘
     */
    protected fun hideKeyBoard(): Boolean {
        val imm = getActivity().applicationContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm?.hideSoftInputFromWindow(getActivity().findViewById<View>(android.R.id.content)
                .windowToken, 0) ?: false
    }


    //============================= 生命周期 ================================

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = onCreateView()
        if (translucentFull()) {
            if (view is QMUIWindowInsetLayout) {
                view.setFitsSystemWindows(false)
                mBaseView = view
            } else {
                mBaseView = QMUIWindowInsetLayout(getActivity())
                (mBaseView as QMUIWindowInsetLayout).addView(view, FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
        } else {
            view.fitsSystemWindows = true
            mBaseView = view
        }
        QMUIViewHelper.requestApplyInsets(getActivity().window)
        return mBaseView
    }

    protected fun popBackStack() {
        baseFragmentActivity!!.popBackStack()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (!enter && parentFragment != null && parentFragment.isRemoving) {
            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            // See https://code.google.com/p/android/issues/detail?id=55228
            val doNothingAnim = AlphaAnimation(1f, 1f)
            doNothingAnim.duration = 300
            return doNothingAnim
        }

        // bugfix: 使用scale enter时看不到效果， 因为两个fragment的动画在同一个层级，被退出动画遮挡了
        // http://stackoverflow.com/questions/13005961/fragmenttransaction-animation-to-slide-in-over-top#33816251
        if (nextAnim != R.anim.scale_enter || !enter) {
            return super.onCreateAnimation(transit, enter, nextAnim)
        }
        try {
            val nextAnimation = AnimationUtils.loadAnimation(context, nextAnim)
            nextAnimation.setAnimationListener(object : Animation.AnimationListener {

                private var mOldTranslationZ: Float = 0.toFloat()

                override fun onAnimationStart(animation: Animation) {
                    if (view != null) {
                        mOldTranslationZ = ViewCompat.getTranslationZ(view)
                        ViewCompat.setTranslationZ(view, 100f)
                    }
                }

                override fun onAnimationEnd(animation: Animation) {
                    if (view != null) {
                        view!!.postDelayed({
                            //延迟回复z-index,如果退出动画更长，这里可能会失效
                            ViewCompat.setTranslationZ(view, mOldTranslationZ)
                        }, 100)

                    }
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            return nextAnimation
        } catch (ignored: Exception) {

        }

        return null
    }

    /**
     * onCreateView
     */
    protected abstract fun onCreateView(): View

    //============================= 新流程 ================================

    /**
     * 沉浸式处理，返回 false，则状态栏下为内容区域，返回 true, 则状态栏下为 padding 区域
     */
    protected fun translucentFull(): Boolean {
        return false
    }

    /**
     * 如果是最后一个Fragment，finish后执行的方法
     */
    fun onLastFragmentFinish(): Any? {
        return null
    }

    /**
     * 转场动画控制
     */
    fun onFetchTransitionConfig(): TransitionConfig {
        return SLIDE_TRANSITION_CONFIG
    }

    ////////界面跳转动画
    class TransitionConfig(val enter: Int, val exit: Int, val popenter: Int, val popout: Int) {

        constructor(enter: Int, popout: Int) : this(enter, 0, 0, popout) {}
    }

    companion object {

        // 资源，放在业务初始化，会在业务层
        protected val SLIDE_TRANSITION_CONFIG = TransitionConfig(
                R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right)


        //============================= UI ================================
        protected val SCALE_TRANSITION_CONFIG = TransitionConfig(
                R.anim.scale_enter, R.anim.slide_still, R.anim.slide_still,
                R.anim.scale_exit)
        private val TAG = BaseFragment::class.java!!.getSimpleName()
    }
}


package com.example.administrator.myapplication.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout

import com.example.administrator.myapplication.utils.QMUIStatusBarHelper


/**
 * 基础的 Activity，配合 [BaseFragment] 使用。
 * Created by gxj on 15/9/14.
 */
abstract class BaseFragmentActivity : AppCompatActivity() {
    private var mFragmentContainer: FrameLayout? = null

    protected abstract val contextViewId: Int

    /**
     * 获取当前的 Fragment。
     */
    val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(contextViewId) as BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        QMUIStatusBarHelper.translucent(this);
        mFragmentContainer = FrameLayout(this)
        mFragmentContainer!!.id = contextViewId
        setContentView(mFragmentContainer)
    }

    override fun onBackPressed() {
        val fragment = currentFragment
        if (fragment != null) {
            popBackStack()
        }
    }

    fun startFragment(fragment: BaseFragment) {
        Log.i(TAG, "startFragment")
        val transitionConfig = fragment.onFetchTransitionConfig()
        val tagName = fragment.javaClass.getSimpleName()
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(transitionConfig.enter, transitionConfig.exit, transitionConfig.popenter, transitionConfig.popout)
                .replace(contextViewId, fragment, tagName)
                .addToBackStack(tagName)
                .commit()
    }

    /**
     * 退出当前的 Fragment。
     */
    fun popBackStack() {
        Log.i(TAG, "popBackStack: getSupportFragmentManager().getBackStackEntryCount() = " + supportFragmentManager.backStackEntryCount)
        if (supportFragmentManager.backStackEntryCount <= 1) {
            val fragment = currentFragment
            if (fragment == null) {
                finish()
                return
            }
            val transitionConfig = fragment.onFetchTransitionConfig()
            val toExec = fragment.onLastFragmentFinish()
            if (toExec != null) {
                if (toExec is BaseFragment) {
                    startFragment(toExec)
                } else if (toExec is Intent) {
                    finish()
                    startActivity(toExec)
                    overridePendingTransition(transitionConfig.popenter, transitionConfig.popout)
                } else {
                    throw Error("can not handle the result in onLastFragmentFinish")
                }
            } else {
                finish()
                overridePendingTransition(transitionConfig.popenter, transitionConfig.popout)
            }
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    /**
     * <pre>
     * 返回到clazz类型的Fragment，
     * 如 Home --> List --> Detail，
     * popBackStack(Home.class)之后，就是Home
     *
     * 如果堆栈没有clazz或者就是当前的clazz（如上例的popBackStack(Detail.class)），就相当于popBackStack()
    </pre> *
     */
    fun popBackStack(clazz: Class<out BaseFragment>) {
        supportFragmentManager.popBackStack(clazz.simpleName, 0)
    }

    /**
     * <pre>
     * 返回到非clazz类型的Fragment
     *
     * 如果上一个是目标clazz，则会继续pop，直到上一个不是clazz。
    </pre> *
     */
    fun popBackStackInclusive(clazz: Class<out BaseFragment>) {
        supportFragmentManager.popBackStack(clazz.simpleName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    companion object {
        private val TAG = "BaseFragmentActivity"
    }
}
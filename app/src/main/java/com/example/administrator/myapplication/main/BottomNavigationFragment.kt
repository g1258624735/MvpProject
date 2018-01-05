package com.example.administrator.myapplication.main

import android.os.Bundle
import android.support.design.internal.BaselineLayout
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.administrator.myapplication.R
import com.example.administrator.myapplication.base.BaseFragment
import com.example.administrator.myapplication.base.BaseRecyclerAdapter
import com.example.administrator.myapplication.dialog.CommonDialog
import com.example.administrator.myapplication.middle.MiddleFragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.regex.Pattern

/**
 * 安卓自带的导航栏
 */
class BottomNavigationFragment : BaseFragment() {
    var list = mutableListOf<BaseFragment>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(): View = layoutInflater.inflate(R.layout.fragment_bottom_navigation, null)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView = view!!.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fragment1 = MyFragment()
        val bundle = Bundle()
        bundle.putString("key", "0")
        fragment1.arguments = bundle
        val fragment2 = MyFragment()
        val bundle1 = Bundle()
        bundle1.putString("key", "1")
        fragment2.arguments = bundle1
        val fragment3 = MyFragment()
        val bundle2 = Bundle()
        bundle2.putString("key", "2")
        fragment3.arguments = bundle2
        list.add(fragment1)
        list.add(fragment2)
        list.add(fragment3)
        childFragmentManager.beginTransaction().add(R.id.container, list[0]).add(R.id.container, list[1]).add(R.id.container, list[2]).show(list[0]).hide(list[1]).hide(list[2]).commitAllowingStateLoss()
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val name = item.title.toString()
            when (name) {
                "首页" -> childFragmentManager.beginTransaction().show(list[0]).hide(list[1]).hide(list[2]).commitAllowingStateLoss()
                "列表" -> childFragmentManager.beginTransaction().show(list[1]).hide(list[0]).hide(list[2]).commitAllowingStateLoss()
                else -> childFragmentManager.beginTransaction().show(list[2]).hide(list[0]).hide(list[1]).commitAllowingStateLoss()
            }
            true
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {
        class MyFragment : BaseFragment() {
            var tv: TextView? = null
            var name="";
            override fun onCreateView(): View {
                 name = arguments.getString("key")
                tv = TextView(activity)
                return tv as TextView
            }

            override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                tv?.text = "你好我是bottomNavigationView"+name
            }
        }
    }

}

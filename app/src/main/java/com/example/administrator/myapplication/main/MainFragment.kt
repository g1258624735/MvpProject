package com.example.administrator.myapplication.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast

import com.example.administrator.myapplication.R
import com.example.administrator.myapplication.base.BaseFragment
import com.example.administrator.myapplication.base.BaseRecyclerAdapter
import com.example.administrator.myapplication.dialog.CommonDialog
import com.example.administrator.myapplication.middle.MiddleFragment
import com.example.administrator.myapplication.mine.MineFragment

import java.util.Arrays
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * 首页
 */
class MainFragment : BaseFragment() {
    private val fragmentTag = arrayOf<Class<*>>(HomeFragment::class.java, MiddleFragment::class.java, MineFragment::class.java)
    private val list_title = arrayOf("第一页", "第二页", "第三页")

    override fun onCreateView(): View {
        return layoutInflater.inflate(R.layout.activity_main, null)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = view!!.findViewById<ViewPager>(R.id.viewPager)
        val tab = view.findViewById<TabLayout>(R.id.tab)
        val mainPagerAdapter = MainPagerAdapter(activity, childFragmentManager, Arrays.asList(*fragmentTag), Arrays.asList(*list_title))
        viewPager.adapter = mainPagerAdapter
        tab.setupWithViewPager(viewPager)
    }

}

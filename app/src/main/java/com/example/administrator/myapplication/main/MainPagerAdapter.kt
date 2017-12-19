package com.example.administrator.myapplication.main

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by gxj on 2017/11/22.
 * viewpager据适配器
 */

class MainPagerAdapter(private val context: Context, fm: FragmentManager, private val fragments: List<Class<*>>?, private val list_title: List<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return if (fragments != null && fragments.size > position) {
            Fragment.instantiate(context, fragments[position].name)
        } else null
    }

    override fun getCount(): Int {
        return fragments?.size ?: 0
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list_title[position]
    }
}

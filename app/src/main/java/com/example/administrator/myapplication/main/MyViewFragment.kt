package com.example.administrator.myapplication.main

import android.os.Bundle
import android.support.design.internal.BaselineLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Switch
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
 * 测试自定义控件
 */
class MyViewFragment : BaseFragment() {

    override fun onCreateView(): View = layoutInflater.inflate(R.layout.fragment_my_home, null)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

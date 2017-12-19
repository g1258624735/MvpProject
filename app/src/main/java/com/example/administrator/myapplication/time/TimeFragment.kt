package com.example.administrator.myapplication.time

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast

import com.example.administrator.myapplication.R
import com.example.administrator.myapplication.base.BaseFragment
import com.example.administrator.myapplication.base.BaseRecyclerAdapter
import com.example.administrator.myapplication.dialog.CommonDialog
import com.example.administrator.myapplication.main.HomeAdapter
import com.example.administrator.myapplication.middle.MiddleFragment

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
 * 我的
 */
class TimeFragment : BaseFragment() {
    private val list = arrayOf("自定义dialog")

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(): View = layoutInflater.inflate(R.layout.activity_home, null)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView)
        val manager = LinearLayoutManager(activity)
        manager.orientation = LinearLayoutManager.VERTICAL
        val adapter = HomeAdapter(activity, Arrays.asList(*list))
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, pos: Int) {
                when (pos) {
                    0 -> CommonDialog.Builder(activity).setTitle("标题").setLeftText("取消").setRightText("确定")
                            .setContent("我是内容").setEnableOneButton(false).setShowEditText(false).setEditTextHint("请输入用户密码")
                            .setGravity(CommonDialog.DialogGravityType.GRAVITY_CENTER)
                            .setAnimationStyle(CommonDialog.DialogAnimationType.DIALOG_ANIMATION_SCALE)
                            .setOneButtonSureOnClickListener(object : CommonDialog.OnCommonClickListener {
                                override fun onClick(dialog: CommonDialog, text: String) =
                                        dialog.dismiss()
                            }).setLeftOnClickListener(object : CommonDialog.OnCommonClickListener {
                        override fun onClick(dialog: CommonDialog, text: String) = dialog.dismiss()
                    }).setRightOnClickListener(object : CommonDialog.OnCommonClickListener {
                        override fun onClick(dialog: CommonDialog, text: String) {
                            dialog.dismiss()
                            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                        }
                    }).create().show()
                    1->startFragment(MiddleFragment())
                }
            }
        })
    }

    /**
     * 测试RXJava 网络回收
     */
    private fun getNetData() {
        compositeDisposable.add(Flowable.just("\\U672a\\U77e5\\U9519\\U8bef").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { s -> Toast.makeText(getActivity(), unicodeToString(s), Toast.LENGTH_SHORT).show() })
    }

    /**
     * 测试RXJava 定时发送任务
     */
    private fun rxJava() {
        compositeDisposable.add(interval(5)
                .subscribe { integer -> Log.e("TAG", "开始网络请求...-" + integer!!) })
    }


    fun interval(time: Int): Observable<Int> {
        var time = time
        val period = 100
        if (time < 0) time = 0
        val countTime = time * period
        return Observable.interval(0, period.toLong(), TimeUnit.MILLISECONDS).delay(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { t -> countTime - period * t!!.toInt() }
                .take(time.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    companion object {


        fun unicodeToString(str: String): String {
            var str = str
            val pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))")
            val matcher = pattern.matcher(str)
            var ch: Char
            while (matcher.find()) {
                ch = Integer.parseInt(matcher.group(2), 16).toChar()
                str = str.replace(matcher.group(1), ch + "")
            }
            return str
        }
    }


}

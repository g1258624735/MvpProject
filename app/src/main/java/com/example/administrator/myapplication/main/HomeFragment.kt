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
 * 主页
 */
class HomeFragment : BaseFragment() {
    private val list = arrayOf("自定义dialog", "测试fragment跳转", "测试自带AlertDialog",
            "测试自定义布局CommonDialog", "BottomNavigationFragment底部导航栏")

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
            override fun onItemClick(itemView: View, pos: Int) = initClick(pos)
        })
    }

    private fun initClick(pos: Int) {
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
            1 -> startFragment(MiddleFragment())
            2 -> {
                AlertDialog.Builder(activity).setMessage("我是内容").setTitle("我是标题").
                        setPositiveButton("取消") { dialog, _ -> dialog?.dismiss() }
                        .setNegativeButton("确定") { dialog, _ -> dialog?.dismiss() }.create().show()
            }
            3 -> {
                val v = layoutInflater.inflate(R.layout.dialog_bottom_home, null)
                val dialog: CommonDialog = CommonDialog.Builder(activity).setView(v)
                        .setGravity(CommonDialog.DialogGravityType.GRAVITY_BOTTOM)
                        .setWidthStyle(CommonDialog.DialogWidthStyle.WIDTH_STYLE_MATCH_PARENT)
                        .setAnimationStyle(CommonDialog.DialogAnimationType.DIALOG_ANIMATION_UP_DOWN)
                        .create()
                v.findViewById<Button>(R.id.cancel).setOnClickListener({
                    dialog.dismiss()
                })
                dialog.show()
            }
            4 -> {
            }
        }

    }

    /**
     * 测试RXJava 网络回收
     */
    private fun getNetData() {
        compositeDisposable.add(Flowable.just("\\U672a\\U77e5\\U9519\\U8bef").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { s -> Toast.makeText(activity, unicodeToString(s), Toast.LENGTH_SHORT).show() })
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

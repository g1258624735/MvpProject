package com.example.administrator.myapplication.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.base.BaseRecyclerAdapter;
import com.example.administrator.myapplication.dialog.CommonDialog;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页
 */
public class MainFragment extends BaseFragment {
    private String[] list = {"自定义dialog"};

    @Override
    protected View onCreateView() {
        return getLayoutInflater().inflate(R.layout.activity_main, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        MainAdapter adapter = new MainAdapter(activity, Arrays.asList(list));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                switch (pos) {
                    case 0:
                        new CommonDialog.Builder(activity).setTitle("标题").setLeftText("取消").setRightText("确定")
                                .setContent("我是内容").setEnableOneButton(false).setIsShowEditText(true).setEditTextHint("请输入用户密码")
                                .setOneButtonSureOnClickListener(new CommonDialog.OnCommonClickListener() {
                                    @Override
                                    public void onClick(CommonDialog dialog,String text) {
                                        dialog.dismiss();
                                    }
                                }).setLeftOnClickListener(new CommonDialog.OnCommonClickListener() {
                            @Override
                            public void onClick(CommonDialog dialog, String text) {
                                dialog.dismiss();
                            }
                        }).setRightOnClickListener(new CommonDialog.OnCommonClickListener() {
                            @Override
                            public void onClick(CommonDialog dialog,String text) {
                                dialog.dismiss();
                                Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();
                        break;
                }
            }
        });
    }

    /**
     * 测试RXJava 网络回收
     */
    private void getNetData() {
        compositeDisposable.add(Flowable.just("\\U672a\\U77e5\\U9519\\U8bef").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(getActivity(), unicodeToString(s), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * 测试RXJava 定时发送任务
     */
    private void rxJava() {
        compositeDisposable.add(interval(5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e("TAG", "开始网络请求...-" + integer);
                    }
                }));
    }


    public Observable<Integer> interval(int time) {
        final int period = 100;
        if (time < 0) time = 0;
        final int countTime = time * period;
        return Observable.interval(0, period, TimeUnit.MILLISECONDS).delay(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        return countTime - period * aLong.intValue();
                    }
                })
                .take(time);
    }


    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


}

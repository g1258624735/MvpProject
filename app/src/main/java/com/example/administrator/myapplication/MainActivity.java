package com.example.administrator.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.base.BaseFragmentActivity;
import com.example.administrator.myapplication.main.MainFragment;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseFragmentActivity implements MainContract.View {
    @Inject
    MainPresenter presenter;

    @Override
    protected int getContextViewId() {
        return R.id.main_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        presenter.loadData();
//        getNetData();
//        rxJava();
        DaggerMainComponent.builder().mainModule(new MainModule(this)).build().inject(this);
        if (savedInstanceState == null) {
            BaseFragment fragment = new MainFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }


    /**
     * 测试RXJava 网络回收
     */
    private void getNetData() {
        compositeDisposable.add(Flowable.just("\\U672a\\U77e5\\U9519\\U8bef").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MainActivity.this, unicodeToString(s), Toast.LENGTH_SHORT).show();
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


    @Override
    public void updateUI(String name) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}

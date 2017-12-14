package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2017/11/14.
 * MainPresenter
 */
public class MainPresenter {
    //MainContract是个接口，View是他的内部接口，这里看做View接口即可
    private MainContract.View mView;
    private User user;
    MainPresenter(MainContract.View view,User user) {
        mView = view;
        this.user = user;
    }

    public void loadData() {
        //调用model层方法，加载数据
        //回调方法成功时
        mView.updateUI(user.getName());
    }
}
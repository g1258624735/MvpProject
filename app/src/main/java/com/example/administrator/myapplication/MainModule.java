package com.example.administrator.myapplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/11/14.
 * MainModule
 */
@Module
class MainModule {

    private final MainContract.View mView;

    MainModule(MainContract.View view) {
        mView = view;
    }

    @Provides
    MainContract.View provideMainView() {
        return mView;
    }

    @Provides
    MainPresenter provideMainPresenter(User user){
        return new MainPresenter(mView,user);
    }

    @Provides
    User provideUser() {
        return new User("你好我是注入");
    }
}

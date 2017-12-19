package com.example.administrator.myapplication

import dagger.Module
import dagger.Provides

/**
 * Created by Administrator on 2017/11/14.
 * MainModule
 */
@Module
internal class MainModule(private val mView: MainContract.View) {

    @Provides
    fun provideMainView(): MainContract.View = mView

    @Provides
    fun provideMainPresenter(user: User): MainPresenter = MainPresenter(mView, user)

    @Provides
    fun provideUser(): User = User("你好我是注入")
}

package com.example.administrator.myapplication

import dagger.Component

/**
 * Created by Administrator on 2017/11/14.
 * MainComponent
 */
@Component(modules = arrayOf(MainModule::class))
internal interface MainComponent {
    val user: User
    fun inject(activity: MainActivity)
}

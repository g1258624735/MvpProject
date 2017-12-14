package com.example.administrator.myapplication;

import dagger.Component;

/**
 * Created by Administrator on 2017/11/14.
 * MainComponent
 */
@Component(modules = MainModule.class)
interface MainComponent {
    void inject(MainActivity activity);
    User getUser();
}

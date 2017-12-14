package com.example.administrator.myapplication;


/**
 * Created by Administrator on 2017/11/14.
 * 数据模型
 */
class User {
private String name;
//    @Inject
     User(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

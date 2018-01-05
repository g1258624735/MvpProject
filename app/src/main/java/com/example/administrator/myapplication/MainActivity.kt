package com.example.administrator.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.example.administrator.myapplication.base.BaseFragment
import com.example.administrator.myapplication.base.BaseFragmentActivity
import com.example.administrator.myapplication.main.MainFragment

import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.inject.Inject

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseFragmentActivity() {

    override val contextViewId: Int
        get() = R.id.main_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = MainFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(contextViewId, fragment, fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName)
                    .commit()
        }
    }
}

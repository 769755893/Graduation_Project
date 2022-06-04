package com.app.project.hotel.common

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<T: ViewBinding>: FragmentActivity(), BindLife {
    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()
    var viewBind: T? = null

    @LayoutRes
    abstract fun provideLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        window.statusBarColor = Color.parseColor("#F5A623")
        viewBind = DataBindingUtil.setContentView(this, provideLayoutId())
        initCase()
        initCaseSecond()
        initCaseThird()
    }
    abstract fun initCase()
    open fun initCaseSecond(){}
    open fun initCaseThird(){}

    override fun onStart() {
        super.onStart()
        initCase()
    }

    override fun onRestart() {
        super.onRestart()
        initCase()
    }

    override fun onResume() {
        super.onResume()
        initCase()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        viewBind = null
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}
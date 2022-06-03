package com.app.project.hotel.common

import android.os.Bundle
import android.view.Window
import com.app.project.hotel.R
import com.app.project.hotel.databinding.ActivityMainBinding

lateinit var mwindow: Window
lateinit var mPackageName: String
abstract class BaseMainActivity: BaseActivity<ActivityMainBinding>() {
    override fun provideLayoutId() = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mwindow = window
        actionBar?.hide()
        mPackageName = packageName
    }
}
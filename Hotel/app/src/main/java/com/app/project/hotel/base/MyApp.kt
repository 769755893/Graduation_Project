package com.app.project.hotel.base

import android.app.Application
import android.content.Context
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.IO
import com.example.uitraning.util.log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

lateinit var myapplicationContext: Context
@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        myapplicationContext = applicationContext
    }
}
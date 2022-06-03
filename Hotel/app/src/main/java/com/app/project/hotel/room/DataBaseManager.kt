package com.app.project.hotel.room

import android.content.Context
import androidx.room.Room

object DataBaseManager {
    private const val DB_NAME = "Login.db"
    private lateinit var applicationContext: Context
    val db by lazy {
        Room.databaseBuilder(applicationContext, DataBase::class.java, DB_NAME)
            .build()
    }
    fun saveApplication(context: Context) {
        applicationContext = context
    }
}
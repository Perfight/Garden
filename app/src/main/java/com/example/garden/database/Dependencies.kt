package com.example.garden.database

import android.content.Context
import androidx.room.Room

object Dependencies {
    private lateinit var applicationContext: Context

    fun init(context: Context) { //конструктор
        applicationContext = context
    }

    private val appDatabase: AppDatabase by lazy { //ленивая инициализаци
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb.db") //название под которым он сохранит внутри устроиства
            .createFromAsset("mydb.db") //создает из шаблона
            .build()
    }

    val gardenRepository: GardenRepository by lazy { GardenRepository(appDatabase.getDao()) }
}
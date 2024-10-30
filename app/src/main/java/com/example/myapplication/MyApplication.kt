package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration


@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this) // Realm'i burada başlatıyoruz
        val config = RealmConfiguration.Builder()
            .name(getString(R.string.yeniledim_db_realm))
            .schemaVersion(2)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}
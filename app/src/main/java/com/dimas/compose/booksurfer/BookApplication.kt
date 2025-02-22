package com.dimas.compose.booksurfer

import android.app.Application
import com.dimas.compose.booksurfer.di.initKoin
import org.koin.android.ext.koin.androidContext

class BookApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BookApplication)
        }
    }
}
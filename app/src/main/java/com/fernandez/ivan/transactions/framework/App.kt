package com.fernandez.ivan.transactions.framework

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application()
{
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(networkModule, dataModule, useCaseModule, viewModelModule))
        }
    }
}
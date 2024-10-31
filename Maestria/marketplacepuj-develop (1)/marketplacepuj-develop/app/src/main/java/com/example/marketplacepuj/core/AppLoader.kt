package com.example.marketplacepuj.core

import android.app.Application
import com.example.marketplacepuj.core.di.appModule
import com.example.marketplacepuj.core.di.loginModule
import com.example.marketplacepuj.core.di.productsModule
import com.example.marketplacepuj.core.di.signUpModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppLoader : Application() {

    companion object {
        lateinit var appContext: AppLoader
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppLoader)
            modules(appModule, loginModule, signUpModule, productsModule)
        }
        appContext = this
    }
}
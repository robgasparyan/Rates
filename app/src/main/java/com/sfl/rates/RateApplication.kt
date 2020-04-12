package com.sfl.rates

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RateApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RateApplication)
            androidLogger(Level.DEBUG)
            modules(listOf(viewModels, appRepositories, services, networkModule))
        }
    }

}
package com.sebaserritella.reflexgame.base

import android.app.Application
import com.sebaserritella.reflexgame.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ReflexGameApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //Plant crash/log reporting tool with Timber
        }
    }
}
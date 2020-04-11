package com.project.covid19

import android.app.Application
import android.app.Service
import android.content.Context
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.project.covid19.di.ApplicationInjector
import com.project.covid19.utils.theme.ThemeHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.HasServiceInjector
import timber.log.Timber
import javax.inject.Inject

class Covid19App: Application(), HasAndroidInjector, HasServiceInjector {
    @Inject
    lateinit var dispatchAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var dispatchServiceInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
        ApplicationInjector.init(this)
        ThemeHelper.applyTheme(ThemeHelper.DEFAULT_MODE)
        AndroidThreeTen.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return this.dispatchAndroidInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
       return this.dispatchServiceInjector
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
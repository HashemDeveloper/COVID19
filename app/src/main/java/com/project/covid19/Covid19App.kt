package com.project.covid19

import android.app.Application
import androidx.databinding.library.BuildConfig
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.project.covid19.di.ApplicationInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class Covid19App: Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
        ApplicationInjector.init(this)
        AndroidThreeTen.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return this.dispatchAndroidInjector
    }
}
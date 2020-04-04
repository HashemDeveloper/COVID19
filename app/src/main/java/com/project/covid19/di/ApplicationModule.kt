package com.project.covid19.di

import android.content.Context
import com.project.covid19.Covid19App
import com.project.covid19.data.local.ISharedPref
import com.project.covid19.data.local.SharedPref
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Singleton
    @Provides
    fun provideContext(app: Covid19App): Context {
        return app
    }
    @Singleton
    @Provides
    fun provideSharedPref(sharedPref: SharedPref): ISharedPref {
        return sharedPref
    }
}
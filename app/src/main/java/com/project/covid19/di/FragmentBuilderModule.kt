package com.project.covid19.di

import com.project.covid19.views.DisplayNews
import com.project.covid19.views.LiveDataMapView
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeShowCaseFragment(): DisplayNews
    @ContributesAndroidInjector
    abstract fun contributeLiveDataMapViewFragment(): LiveDataMapView
}
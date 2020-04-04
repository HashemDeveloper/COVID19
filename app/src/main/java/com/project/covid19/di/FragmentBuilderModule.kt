package com.project.covid19.di

import com.project.covid19.views.LiveDataMapView
import com.project.covid19.views.ShowCase
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeShowCaseFragment(): ShowCase
    @ContributesAndroidInjector
    abstract fun contributeLiveDataMapViewFragment(): LiveDataMapView
}
package com.project.covid19.di

import com.project.covid19.MainActivityCovid19
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun contributeActivityModule(): MainActivityCovid19
}
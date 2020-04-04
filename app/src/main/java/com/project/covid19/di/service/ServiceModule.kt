package com.project.covid19.di.service

import com.project.covid19.di.scops.ServiceKey
import com.project.covid19.services.LocationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ServiceKey
    @ContributesAndroidInjector
    abstract fun contributeLocationService(): LocationService
}
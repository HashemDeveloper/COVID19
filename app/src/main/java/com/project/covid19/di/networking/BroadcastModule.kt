package com.project.covid19.di.networking

import com.project.covid19.broadcast.Covid19BroadCastReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastModule {
    @ContributesAndroidInjector
    abstract fun contributeBroadcastReceiver(): Covid19BroadCastReceiver
}
package com.project.covid19.di

import com.project.covid19.Covid19App
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ActivityModule::class, ApplicationModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun buildApplication(app: Covid19App): Builder
        fun build(): ApplicationComponent
    }
    fun inject(app: Covid19App)
}
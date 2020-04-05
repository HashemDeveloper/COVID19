package com.project.covid19.di

import com.project.covid19.Covid19App
import com.project.covid19.di.networking.RetrofitModule
import com.project.covid19.di.service.ServiceModule
import com.project.covid19.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,
    ActivityModule::class, ViewModelModule::class, ServiceModule::class, RetrofitModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun buildApplication(app: Covid19App): Builder
        fun build(): ApplicationComponent
    }
    fun inject(app: Covid19App)
}
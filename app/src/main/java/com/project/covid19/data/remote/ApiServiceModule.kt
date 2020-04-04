package com.project.covid19.data.remote

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object ApiServiceModule {
    @Singleton
    @Provides
    @JvmStatic
    internal fun provideCovidAPIModule(retrofit: Retrofit): CovidAPI {
        return retrofit.create(CovidAPI::class.java)
    }
}
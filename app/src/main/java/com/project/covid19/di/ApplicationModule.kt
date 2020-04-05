package com.project.covid19.di

import android.content.Context
import com.project.covid19.Covid19App
import com.project.covid19.data.local.*
import com.project.covid19.data.remote.Covid19Repo
import com.project.covid19.data.remote.ICovid19Repo
import com.project.covid19.utils.search.ISearchSuggestion
import com.project.covid19.utils.search.SearchSuggestion
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
    @Singleton
    @Provides
    fun provideCovidRepository(covid19Repo: Covid19Repo): ICovid19Repo {
        return covid19Repo
    }
    //Local Data
    @Singleton
    @Provides
    fun provideHopkinsDataDao(dbProvider: LocalDbServiceProvider): IHopkinsDataDao {
        return dbProvider.getHopkinsDataDao()
    }

    //Local data
    @Singleton
    @Provides
    fun provideHopkinsDataRepo(hopkinsDataRepo: HopkinsDataRepo): IHopkinsDataRepo {
        return hopkinsDataRepo
    }
    @Singleton
    @Provides
    fun provideSearchSuggestion(searchSuggestion: SearchSuggestion): ISearchSuggestion {
        return searchSuggestion
    }
}
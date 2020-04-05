package com.project.covid19.di

import android.content.Context
import com.project.covid19.Covid19App
import com.project.covid19.data.local.*
import com.project.covid19.data.remote.Covid19Repo
import com.project.covid19.data.remote.ICovid19Repo
import com.project.covid19.utils.networkconnections.ConnectionStateMonitor
import com.project.covid19.utils.networkconnections.IConnectionStateMonitor
import com.project.covid19.utils.networkconnections.IUpdateNetLowApiHelper
import com.project.covid19.utils.networkconnections.UpdateNetLowApiHelper
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
    @Singleton
    @Provides
    fun provideCSSESearchHistoryDao(dbProvider: LocalDbServiceProvider): ICsseSearchDao {
        return dbProvider.getCSSESearchHistoryDao()
    }

    //Local data
    @Singleton
    @Provides
    fun provideHopkinsDataRepo(hopkinsDataRepo: HopkinsDataRepo): IHopkinsDataRepo {
        return hopkinsDataRepo
    }
    @Singleton
    @Provides
    fun provideCSSESearchRepo(csseSearchRepo: CSSESearchRepo): ICSSESearchRepo {
        return csseSearchRepo
    }
    @Singleton
    @Provides
    fun provideSearchSuggestion(searchSuggestion: SearchSuggestion): ISearchSuggestion {
        return searchSuggestion
    }
    @Singleton
    @Provides
    fun provideConnectionStateMonitor(connectionStateMonitor: ConnectionStateMonitor): IConnectionStateMonitor {
        return connectionStateMonitor
    }
    @Singleton
    @Provides
    fun provideNetworkLowApiHelper(updateNetLowApiHelper: UpdateNetLowApiHelper): IUpdateNetLowApiHelper {
        return updateNetLowApiHelper
    }
}
package com.project.covid19.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.covid19.di.scops.ViewModelKey
import com.project.covid19.viewmodels.LiveDataMapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
    @Binds
    @IntoMap
    @ViewModelKey(LiveDataMapViewModel::class)
    abstract fun provideLiveDataMapViewModel(liveDataMapViewModel: LiveDataMapViewModel): ViewModel
}
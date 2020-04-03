package com.project.covid19.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(private val mCreator: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Provider<out  ViewModel>? = this.mCreator[modelClass]
        if (creator == null) {
            for ((key: Class<out ViewModel>, value: Provider<ViewModel>) in this.mCreator) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        requireNotNull(creator) {
            "Unknown model class $modelClass"
        }
        try {
            return creator.get() as T
        } catch (ex: Exception) {
            throw RuntimeException()
        }
    }
}
package com.project.covid19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arlib.floatingsearchview.FloatingSearchView
import com.project.covid19.data.remote.DataHandler
import com.project.covid19.data.remote.ICovid19Repo
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.utils.search.ISearchSuggestion
import com.project.covid19.utils.search.SearchSuggestion
import javax.inject.Inject

class LiveDataMapViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var covid19Repo: ICovid19Repo
    @Inject
    lateinit var iSearchSuggestion: ISearchSuggestion
    private var cssDataLiveData: LiveData<DataHandler<List<HopkinsCSSEDataRes>>>?= null

    fun getCssDataLiveData(): LiveData<DataHandler<List<HopkinsCSSEDataRes>>>? {
        this.cssDataLiveData = this.covid19Repo.getCSSELiveData()
        return cssDataLiveData
    }
    fun fetchAndSaveData() {
        this.covid19Repo.fetchAndSaveCSSEData()
    }

    fun findSuggestions(newQuery: String, searchView: FloatingSearchView?) {
        searchView?.showProgress()
        this.iSearchSuggestion.findSuggestions(newQuery, 5, object : SearchSuggestion.SearchSuggestionListener {
            override fun onSearchResult(result: List<HopkinsCSSEDataRes>) {
                searchView?.swapSuggestions(result)
                searchView?.hideProgress()
            }
        })
    }

    fun setupSearchHistory(liveDataSearchViewId: FloatingSearchView?) {
        //TODO
    }
}
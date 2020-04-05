package com.project.covid19.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.covid19.data.remote.DataHandler
import com.project.covid19.data.remote.ICovid19Repo
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import javax.inject.Inject

class LiveDataMapViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var covid19Repo: ICovid19Repo
    private var cssDataLiveData: LiveData<DataHandler<List<HopkinsCSSEDataRes>>>?= null

    fun getCssDataLiveData(): LiveData<DataHandler<List<HopkinsCSSEDataRes>>>? {
        this.cssDataLiveData = this.covid19Repo.getCSSELiveData()
        return cssDataLiveData
    }
    fun fetchAndSaveData() {
        this.covid19Repo.fetchAndSaveCSSEData()
    }
}
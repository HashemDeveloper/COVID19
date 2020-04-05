package com.project.covid19.data.remote

import androidx.lifecycle.LiveData
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes

interface ICovid19Repo {
    fun getCSSELiveData(): LiveData<DataHandler<List<HopkinsCSSEDataRes>>>?
    fun fetchAndSaveCSSEData()
}
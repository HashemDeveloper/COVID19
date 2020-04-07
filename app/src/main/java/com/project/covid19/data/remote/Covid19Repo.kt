package com.project.covid19.data.remote

import androidx.lifecycle.LiveData
import com.project.covid19.BuildConfig
import com.project.covid19.data.local.IHopkinsDataRepo
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.model.smartableai.COVIDSmartTableAIRes
import com.project.covid19.utils.extensions.covidLiveDataExt
import com.project.covid19.utils.extensions.fetchAndSaveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class Covid19Repo @Inject constructor(): ICovid19Repo, BaseDataSource(), CoroutineScope {
    @Inject
    lateinit var api: CovidAPI
    @Inject
    lateinit var hopkinsDataRepo: IHopkinsDataRepo
    private val job = Job()

    /**
     * This method will be called initially to fetch Hopkins CSSE Data
     * We will be using this data to display suggestions on @see LiveDataMapView
     */
    override fun fetchAndSaveCSSEData() {
        launch {
            fetchAndSaveData(call = {
                api.fetchCSSEData()
            }, onSuccess = {
                saveData(it)
            }, onError = {
                debug(it)
            })
        }
    }

    override fun getCSSELiveData(): LiveData<DataHandler<List<HopkinsCSSEDataRes>>>? {
        return covidLiveDataExt {
            callFetchCSSEDataApi()!!
        }
    }

    private fun saveData(list: List<HopkinsCSSEDataRes>) {
        this.hopkinsDataRepo.deleteAll()
        val dataList: MutableList<HopkinsCSSEDataRes> = arrayListOf()
        for (hopkinsData in list) {
            val uniqueId: String = UUID.randomUUID().toString()
            hopkinsData.id = uniqueId
            dataList.add(hopkinsData)
        }
        this.hopkinsDataRepo.insert(dataList)
    }
    private fun debug(error: String) {
        if (BuildConfig.DEBUG) {
            Timber.e(TAG, "Error: $error")
        }
    }

    private suspend fun callFetchCSSEDataApi(): DataHandler<List<HopkinsCSSEDataRes>>? {
        return getResult {
            this.api.fetchCSSEData()
        }
    }

    override fun getCOVID19News(url: String): LiveData<DataHandler<COVIDSmartTableAIRes>>? {
        return covidLiveDataExt {
            callSmartableCOVIDNewsAPI(url)!!
        }
    }

    private suspend fun callSmartableCOVIDNewsAPI(url: String): DataHandler<COVIDSmartTableAIRes>? {
        return getResult {
            this.api.getNewsOfCOVID19(url)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

    companion object {
        private var TAG: String = Covid19Repo::class.java.canonicalName!!
    }
}
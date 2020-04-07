package com.project.covid19.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.arlib.floatingsearchview.FloatingSearchView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.project.covid19.data.remote.DataHandler
import com.project.covid19.data.remote.ICovid19Repo
import com.project.covid19.events.DrawerLayoutEvent
import com.project.covid19.model.hopkinsdata.Coordinates
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.model.hopkinsdata.SearchHopkinData
import com.project.covid19.model.smartableai.COVIDSmartTableAIRes
import com.project.covid19.utils.Constants
import com.project.covid19.utils.rxevents.IRxEvents
import com.project.covid19.utils.search.ISearchSuggestion
import com.project.covid19.utils.search.SearchSuggestion
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class LiveDataMapViewModel @Inject constructor(): ViewModel() {
    private var confirmedCircle: Circle?= null
    private var deathStatCircle: Circle?= null
    @Inject
    lateinit var iRxEvents: IRxEvents
    @Inject
    lateinit var covid19Repo: ICovid19Repo
    @Inject
    lateinit var iSearchSuggestion: ISearchSuggestion
    private var cssDataLiveData: LiveData<DataHandler<List<HopkinsCSSEDataRes>>>?= null
    private var covid19NewsData: LiveData<DataHandler<COVIDSmartTableAIRes>>?= null

    fun fetchAndSaveData() {
        this.covid19Repo.fetchAndSaveCSSEData()
    }

    fun getCOVID19NewsLiveData(location: String): LiveData<DataHandler<COVIDSmartTableAIRes>>? {
        this.covid19NewsData = this.covid19Repo.getCOVID19News(location)
        return this.covid19NewsData
    }


    fun findSuggestions(newQuery: String, searchView: FloatingSearchView?) {
        searchView?.showProgress()
        this.iSearchSuggestion.findSuggestions(newQuery, 5, object : SearchSuggestion.SearchSuggestionListener {
            override fun onSearchResult(result: List<SearchHopkinData>) {
                searchView?.swapSuggestions(result)
                searchView?.hideProgress()
            }
        })
    }

    fun setupSearchHistory(liveDataSearchViewId: FloatingSearchView?) {
        val searchHistory: List<SearchHopkinData>?= this.iSearchSuggestion.getHistory()
        searchHistory?.let { list ->
            liveDataSearchViewId?.swapSuggestions(list)
        }
    }

    fun postDataByState(state: String): HopkinsCSSEDataRes? {
       return this.iSearchSuggestion.getItemByState(state)
    }

    fun saveSearchHistory(hopkinsCSSData: SearchHopkinData) {
        val date: OffsetDateTime = Constants.getCurrentTime()
        hopkinsCSSData.isHistory = true
        hopkinsCSSData.date = date
        this.iSearchSuggestion.saveSuggestion(hopkinsCSSData)
    }

    fun setupDrawerLayout(liveDataSearchViewId: FloatingSearchView?) {
        this.iRxEvents.post(DrawerLayoutEvent(liveDataSearchViewId!!))
    }

    fun drawVisualData(googleMap: GoogleMap) {
        val dataList: List<HopkinsCSSEDataRes>?= this.iSearchSuggestion.getAllHopkingsCSSEData()
        dataList?.let { list ->
            for (data: HopkinsCSSEDataRes in list) {
                data.coordinates?.let { coor ->
                    val coordinate: Coordinates? = coor
                    coordinate?.let {ltln ->
                        if (ltln.lattitude.isNotEmpty() && ltln.longitude.isNotEmpty()) {
                            val latLong: LatLng = LatLng(ltln.lattitude.toDouble(), ltln.longitude.toDouble())
                            var confirmedRD: Double = 0.0
                            var deathRD: Double = 0.0
                            data.stats?.let { stats ->
                                if (stats.confirmed.isNotEmpty()) {
                                    confirmedRD = stats.confirmed.toDouble()
                                }
                                if (stats.deaths.isNotEmpty()) {
                                    deathRD = stats.deaths.toDouble()
                                }
                            }
                            this.confirmedCircle = googleMap.addCircle(
                                CircleOptions()
                                    .center(latLong)
                                    .radius(confirmedRD)
                                    .strokeWidth(2.0f)
                                    .strokeColor(Color.RED)
                                    .fillColor(Color.argb(128, 255, 0, 0))
                                    .clickable(true))
                            this.deathStatCircle = googleMap.addCircle(
                                CircleOptions()
                                    .center(latLong)
                                    .radius(deathRD)
                                    .strokeWidth(2.0f)
                                    .strokeColor(Color.RED)
                                    .fillColor(Color.argb(128, 255, 0, 100))
                                    .clickable(true))

                        }
                    }
                }
            }
        }
    }


    fun getDataByCoordinates(coordinates: Coordinates): HopkinsCSSEDataRes? {
        return this.iSearchSuggestion.getHopkingsDataByCoordinates(coordinates)
    }
}
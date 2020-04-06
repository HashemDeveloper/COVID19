package com.project.covid19.data.local

import com.project.covid19.model.hopkinsdata.Coordinates
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes

interface IHopkinsDataRepo {
    fun insert(list: List<HopkinsCSSEDataRes>)
    fun getAllData(): List<HopkinsCSSEDataRes>?
    fun getCSSEDataByState(state: String): HopkinsCSSEDataRes?
    fun saveSearchHistory(hopkinsCSSEDataRes: HopkinsCSSEDataRes)
    fun getSearchHistories(): List<HopkinsCSSEDataRes>?
    fun deleteAll()
    fun getCSSEDataByCoordinates(coordinates: Coordinates): HopkinsCSSEDataRes?
}
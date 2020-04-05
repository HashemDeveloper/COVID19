package com.project.covid19.data.local

import com.project.covid19.model.hopkinsdata.SearchHopkinData

interface ICSSESearchRepo {
    fun saveSearchHistory(searchHopkinData: SearchHopkinData)
    fun getSearchHistories(): List<SearchHopkinData>?
    fun deleteAllSearchHistory()
}
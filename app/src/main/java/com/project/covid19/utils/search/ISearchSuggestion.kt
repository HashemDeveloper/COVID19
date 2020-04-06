package com.project.covid19.utils.search

import com.project.covid19.model.hopkinsdata.Coordinates
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.model.hopkinsdata.SearchHopkinData

interface ISearchSuggestion {
    fun findSuggestions(query: String, limit: Int, listener: SearchSuggestion.SearchSuggestionListener)
    fun saveSuggestion(searchHistory: SearchHopkinData)
    fun getHistory(): List<SearchHopkinData>?
    fun getItemByState(state: String): HopkinsCSSEDataRes?
    fun getAllHopkingsCSSEData(): List<HopkinsCSSEDataRes>?
    fun getHopkingsDataByCoordinates(coordinates: Coordinates): HopkinsCSSEDataRes?
}
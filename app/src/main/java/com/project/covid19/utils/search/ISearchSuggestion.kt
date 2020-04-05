package com.project.covid19.utils.search

import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes

interface ISearchSuggestion {
    fun findSuggestions(query: String, limit: Int, listener: SearchSuggestion.SearchSuggestionListener)
    fun saveSuggestion()
    fun getHistory(): List<HopkinsCSSEDataRes>?
    fun getItemByState(state: String): HopkinsCSSEDataRes?
}
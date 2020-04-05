package com.project.covid19.utils.search

import android.widget.Filter
import com.project.covid19.data.local.CSSESearchRepo
import com.project.covid19.data.local.IHopkinsDataRepo
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.model.hopkinsdata.SearchHopkinData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SearchSuggestion @Inject constructor(): ISearchSuggestion, CoroutineScope{
    @Inject
    lateinit var iCSSESearchRepo: CSSESearchRepo
    @Inject
    lateinit var iHopkinsDataRepo: IHopkinsDataRepo
    private val job = Job()

    override fun findSuggestions(query: String, limit: Int, listener: SearchSuggestionListener) {
        object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val suggestionList: MutableList<SearchHopkinData> = arrayListOf()
                if (!(constraint == null || constraint.isEmpty())) {
                    getSuggestionList()?.let {list ->
                        for (hopkinsData: HopkinsCSSEDataRes in list) {
                            val state: String = if (hopkinsData.province != null) hopkinsData.province!! else ""
                            val country: String = if (hopkinsData.country != null) hopkinsData.country!! else ""
                            if ((country.startsWith(constraint.toString(), true))
                                || state.startsWith(constraint.toString(), true)) {
                                val searchData: SearchHopkinData = SearchHopkinData(hopkinsData.id,country,
                                state, hopkinsData.updatedAt, hopkinsData.stats, hopkinsData.coordinates, hopkinsData.isHistory)
                                suggestionList.add(searchData)
                                if (limit != -1 && suggestionList.size == limit) {
                                    break
                                }
                            }
                        }
                    }
                }
                val result = FilterResults()
                suggestionList.sortWith(Comparator { t, t2 ->
                    if (t.isHistory) -1 else 0
                })
                result.values = suggestionList
                result.count = suggestionList.size
                return result
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
                result?.let { filterResults: FilterResults ->
                    if (filterResults.values != null) {
                        listener.onSearchResult(filterResults.values as MutableList<SearchHopkinData>)
                    }
                }
            }
        }.filter(query)
    }

    override fun saveSuggestion(hopkinsCSSEDataRes: HopkinsCSSEDataRes) {
        this.iHopkinsDataRepo.saveSearchHistory(hopkinsCSSEDataRes)
    }

    override fun getHistory(): List<HopkinsCSSEDataRes>? {
        return this.iHopkinsDataRepo.getSearchHistories()
    }

    override fun getItemByState(state: String): HopkinsCSSEDataRes? {
        return this.iHopkinsDataRepo.getCSSEDataByState(state)
    }

    private fun getSuggestionList(): List<HopkinsCSSEDataRes>? {
        return this.iHopkinsDataRepo.getAllData()
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO

    interface SearchSuggestionListener {
        fun onSearchResult(result: List<SearchHopkinData>)
    }
}
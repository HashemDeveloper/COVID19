package com.project.covid19.data.local

import com.project.covid19.BuildConfig
import com.project.covid19.model.hopkinsdata.SearchHopkinData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CSSESearchRepo @Inject constructor(): ICSSESearchRepo, CoroutineScope {
    private val job = Job()
    @Inject
    lateinit var iCsseSearchDao: ICsseSearchDao

    override fun saveSearchHistory(searchHopkinData: SearchHopkinData) {
        launch {
            val insertId: Flow<Long>? = saveData(searchHopkinData)
            insertId?.collect { id ->
                if (BuildConfig.DEBUG) {
                    Timber.d("Saved history successfully with id :$id")
                }
            }
        }
    }

    override fun getSearchHistories(): List<SearchHopkinData>? {
        return retrieveHistoryBlocking()
    }

    override fun deleteAllSearchHistory() {
        TODO("Not yet implemented")
    }

    private fun retrieveHistoryBlocking(): List<SearchHopkinData>? {
        var result: List<SearchHopkinData>?= null
        runBlocking {
            val job: Deferred<List<SearchHopkinData>> = async { retrieveHistory()!! }
            result = job.await()
        }
        return result
    }
    private suspend fun retrieveHistory(): List<SearchHopkinData>? {
        return this.iCsseSearchDao.getHistory()
    }
    private fun saveData(searchHopkinData: SearchHopkinData): Flow<Long> = flow {
        val id: Long = iCsseSearchDao.saveHistory(searchHopkinData)
        emit(id)
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}
package com.project.covid19.data.local

import com.project.covid19.BuildConfig
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HopkinsDataRepo @Inject constructor(): IHopkinsDataRepo, CoroutineScope {
    @Inject
    lateinit var iHopkinsDataDao: IHopkinsDataDao
    private val job = Job()

    override fun insert(list: List<HopkinsCSSEDataRes>) {
       launch {
           val insert: Flow<List<Long>>? = insertData(list)
           insert?.collect {value ->
               val idList: List<Long>? = value
               if (idList != null && idList.isNotEmpty()) {
                   if (BuildConfig.DEBUG) {
                       Timber.d("Save success with id $idList")
                   }
               }
           }
       }
    }

    private fun insertData(list: List<HopkinsCSSEDataRes>): Flow<List<Long>> = flow {
        val value: List<Long> = iHopkinsDataDao.insertHopkinsData(list)
        emit(value)
    }

    override fun getAllData(): List<HopkinsCSSEDataRes>? {
        return getHopkinsDataBlocking()
    }

    override fun getCSSEDataByState(state: String): HopkinsCSSEDataRes? {
        return getCSSEDataByStateBlocking(state)
    }
    private fun getCSSEDataByStateBlocking(state: String): HopkinsCSSEDataRes? {
        var result: HopkinsCSSEDataRes?= null
        runBlocking {
            val job: Deferred<HopkinsCSSEDataRes> = async { getDataByState(state)!! }
            result = job.await()
        }
        return result
    }
    private suspend fun getDataByState(state: String): HopkinsCSSEDataRes? {
        return this.iHopkinsDataDao.getCSSEDataByState(state)
    }
    private fun getHopkinsDataBlocking(): List<HopkinsCSSEDataRes>? {
        var result: List<HopkinsCSSEDataRes>?= null
        runBlocking {
            if (retrieveAllData() != null) {
                val job: Deferred<List<HopkinsCSSEDataRes>> = async { retrieveAllData()!! }
                result = job.await()
            }
        }
        return result
    }
    private suspend fun retrieveAllData(): List<HopkinsCSSEDataRes>? {
        return this.iHopkinsDataDao.getAllHopkinsData()
    }

    override fun deleteAll() {
        launch {
            val delete: Flow<Int>? = deleteAllData()
            delete?.collect {value ->
                val id: Int? = value
                if (id != null) {
                    if (BuildConfig.DEBUG) {
                        Timber.d("Deleted all data successfully $id")
                    }
                }
            }
        }
    }

    private fun deleteAllData(): Flow<Int> = flow {
        val value: Int = iHopkinsDataDao.deleteHopkinsData()
        emit(value)
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}
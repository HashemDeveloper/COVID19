package com.project.covid19.data.local

import androidx.room.*
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes

@Dao
interface IHopkinsDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHopkinsData(list: List<HopkinsCSSEDataRes>): List<Long>
    @Transaction @Query("select * from hopkins_css_data order by updatedAt")
    suspend fun getAllHopkinsData(): List<HopkinsCSSEDataRes>
    @Transaction @Query("delete from hopkins_css_data")
    suspend fun deleteHopkinsData(): Int
}
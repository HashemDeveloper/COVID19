package com.project.covid19.data.local

import androidx.room.*
import com.project.covid19.model.hopkinsdata.SearchHopkinData

@Dao
interface ICsseSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHistory(searchHopkinData: SearchHopkinData): Long
    @Transaction @Query("select * from csse_search_history order by updatedAt desc limit 5")
    suspend fun getHistory(): List<SearchHopkinData>
    @Transaction @Query("delete from csse_search_history")
    suspend fun deleteHistory(): Int
}
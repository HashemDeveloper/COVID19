package com.project.covid19.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.model.hopkinsdata.SearchHopkinData
import com.project.covid19.utils.typeconvert.CoordinateConverter
import com.project.covid19.utils.typeconvert.StatConverter

@Database(entities = [HopkinsCSSEDataRes::class, SearchHopkinData::class], version = 1, exportSchema = false)
@TypeConverters(StatConverter::class, CoordinateConverter::class)
abstract class Covid19LocalDb: RoomDatabase() {
    abstract fun getHopkinsDataDao(): IHopkinsDataDao
    abstract fun getHopkingDataSearchHistoryDao(): ICsseSearchDao

    companion object {
        private const val DB_NAME = "COVID19_DB"
        @Volatile
        private var instance: Covid19LocalDb?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildLocalDb(context).also {
                instance = it
            }
        }
        private fun buildLocalDb(context: Context) = Room.databaseBuilder(context.applicationContext,
            Covid19LocalDb::class.java,
            DB_NAME
        ).build()
    }
}
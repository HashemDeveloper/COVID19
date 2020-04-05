package com.project.covid19.utils.typeconvert

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.covid19.model.hopkinsdata.Stats
import java.lang.reflect.Type

class StatConverter {
    @TypeConverter
    fun stringToStat(json: String): Stats? {
        val gson = Gson()
        val type: Type = object : TypeToken<Stats>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun StatsToString(stats: Stats?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<Stats>(){}.type
        return gson.toJson(stats, type)
    }
}
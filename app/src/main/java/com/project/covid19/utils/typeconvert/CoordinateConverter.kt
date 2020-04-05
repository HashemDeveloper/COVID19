package com.project.covid19.utils.typeconvert

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.covid19.model.hopkinsdata.Coordinates
import java.lang.reflect.Type

class CoordinateConverter {
    @TypeConverter
    fun stringToCoordinator(json: String): Coordinates? {
        val gson = Gson()
        val type: Type = object : TypeToken<Coordinates>(){}.type
        return gson.fromJson(json, type)
    }
    @TypeConverter
    fun coordinateToString(coordinates: Coordinates?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<Coordinates>(){}.type
        return gson.toJson(coordinates, type)
    }
}
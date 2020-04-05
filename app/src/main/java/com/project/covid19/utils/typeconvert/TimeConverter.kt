package com.project.covid19.utils.typeconvert

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class TimeConverter {
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffSetDateTime(dateVal: String?): OffsetDateTime {
        return dateTimeFormatter.parse(dateVal, OffsetDateTime::from)
    }

    @TypeConverter
    fun fromOffSetDateTime(dateTime: OffsetDateTime?): String {
        return dateTimeFormatter.format(dateTime)
    }
}
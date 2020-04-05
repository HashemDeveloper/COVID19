package com.project.covid19.model.hopkinsdata

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "hopkins_css_data", indices = [Index(value = ["id"], unique = true)])
@Parcelize
data class HopkinsCSSEDataRes(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "country")
    @SerializedName("country")
    @Expose
    var country: String?,
    @ColumnInfo(name = "province")
    @SerializedName("province")
    @Expose
    var province: String?,
    @ColumnInfo(name = "updatedAt")
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String?,
    @ColumnInfo(name = "stats")
    @SerializedName("stats")
    @Expose
    var stats: @RawValue Stats?,
    @ColumnInfo(name = "coordinates")
    @SerializedName("coordinates")
    @Expose
    var coordinates: @RawValue Coordinates?,
    @ColumnInfo(name = "is_history")
    var isHistory: Boolean
): Parcelable, SearchSuggestion {
    override fun getBody(): String {
        return this.country + (if (province != null) ", $province" else "")
    }
}
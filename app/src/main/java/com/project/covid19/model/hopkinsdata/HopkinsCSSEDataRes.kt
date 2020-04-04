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

@Entity(tableName = "hopkins_css_data", indices = [Index(value = ["id"], unique = true)])
@Parcelize
data class HopkinsCSSEDataRes(
    @PrimaryKey
    @ColumnInfo(name = "id")
    private var id: String,
    @ColumnInfo(name = "country")
    @SerializedName("country")
    @Expose
    private var country: String?,
    @ColumnInfo(name = "province")
    @SerializedName("province")
    @Expose
    private var province: String?,
    @ColumnInfo(name = "updatedAt")
    @SerializedName("updatedAt")
    @Expose
    private var updatedAt: String?,
    @ColumnInfo(name = "stats")
    @SerializedName("stats")
    @Expose
    private var stats: Stats?,
    @ColumnInfo(name = "coordinates")
    @SerializedName("coordinates")
    @Expose
    private var coordinates: Coordinates?
): Parcelable, SearchSuggestion {
    override fun getBody(): String {
        return this.country + (if (province != null) ", $province" else "")
    }
}
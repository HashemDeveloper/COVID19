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
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "csse_search_history", indices = [Index(value = ["id"], unique = true)])
@Parcelize
data class SearchHopkinData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "country")
    var country: String?,
    @ColumnInfo(name = "province")
    var province: String?= "",
    @ColumnInfo(name = "date")
    var date: OffsetDateTime?,
    @ColumnInfo(name = "updatedAt")
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String?,
    @ColumnInfo(name = "stats")
    var stats: @RawValue Stats?,
    @ColumnInfo(name = "coordinates")
    var coordinates: @RawValue Coordinates?,
    @ColumnInfo(name = "is_history")
    var isHistory: Boolean
): Parcelable, SearchSuggestion {
    override fun getBody(): String {
        return if (province != null) {
            this.country + ", $province"
        } else {
            this.country!!
        }
    }
}
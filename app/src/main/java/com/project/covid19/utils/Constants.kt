package com.project.covid19.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object {
        fun hideKeyboard(activity: FragmentActivity?) {
            val imm: InputMethodManager? = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
        fun getCurrentTime(): OffsetDateTime {
            val currentDate = Date()
            val dateFormat: DateFormat = SimpleDateFormat("MMM dd, yyyy, kk:mm", Locale.getDefault())
            val dateGameSaved: String = dateFormat.format(currentDate)
            val dateTimeFormatter: DateTimeFormatter = org.threeten.bp.format.DateTimeFormatterBuilder()
                .parseStrict()
                .appendPattern("MMM dd, uuuu, kk:mm")
                .toFormatter()
                .withResolverStyle(org.threeten.bp.format.ResolverStyle.STRICT)
            val localDate: LocalDate = LocalDate.parse(dateGameSaved, dateTimeFormatter)
            return OffsetDateTime.of(localDate, LocalTime.now(), ZoneOffset.UTC)
        }
    }
}
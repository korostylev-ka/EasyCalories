package ru.korostylev.easycalories.utils

import android.content.Context
import androidx.annotation.StringRes
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.dto.DayOfWeek
import ru.korostylev.easycalories.dto.Month
import kotlin.math.ceil

object AndroidUtils {
    fun dp(context: Context, dp: Float): Int =
        ceil(context.resources.displayMetrics.density * dp).toInt()


    fun getDayOfWeek(_day: Int): Int {
        val days = DayOfWeek.values()
        @StringRes var dayName: Int? = null
        for (day in days) {
            if (day.dayNumber == _day) {
                dayName = day.dayName
            }
        }
        return dayName ?: throw Exception("Day no found")
    }

    fun getMonth(_month: Int): Int {
        val months = Month.values()
        @StringRes var monthName: Int? = null
        for (month in months) {
            if (month.monthNumber == _month) {
                monthName = month.monthName
            }
        }
        return monthName ?: throw Exception("Month no found")
    }

    fun calculateCalories(proteins: Float, fats: Float, carbs: Float): Float {
        return (proteins * 4 + fats * 9 + carbs * 4)
    }
}
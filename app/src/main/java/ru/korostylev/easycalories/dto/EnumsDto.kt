package ru.korostylev.easycalories.dto

import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import ru.korostylev.easycalories.R


enum class DayOfWeek(val dayNumber: Int, @StringRes val dayName: Int) {
    MON(2, R.string.mon), TUE(3, R.string.tue), WED(4, R.string.wed), THU(5, R.string.thu),
    FRI(6, R.string.fri), SAT(7, R.string.sat), SUN(1, R.string.sun);
}

enum class Month(val monthNumber: Int, @StringRes val monthName: Int) {
    JAN(1, R.string.jan), FEB(2, R.string.feb), MAR(3, R.string.mar), APR(4, R.string.apr),
    MAY(5, R.string.may), JUN(6, R.string.jun), JUL(7, R.string.jul), AUG(8, R.string.aug),
    SEP(9, R.string.sep), OCT(10, R.string.oct), NOV(11, R.string.nov), DEC(12, R.string.dec)
}
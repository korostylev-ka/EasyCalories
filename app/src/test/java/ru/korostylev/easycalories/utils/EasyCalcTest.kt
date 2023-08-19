package ru.korostylev.easycalories.utils

import android.content.res.Resources
import org.junit.Assert.*
import ru.korostylev.easycalories.R

import org.junit.Test

class EasyCalcTest {

    @Test
    fun getDayOfWeek() {
        val dayOfWeek = AndroidUtils.getDayOfWeek(1)
        val day = R.string.sun
        assertEquals(day, dayOfWeek)
    }
}
package ru.korostylev.easycalories.application

import android.app.Application
import android.widget.Toast
import com.my.tracker.MyTracker

val SDK_KEY = "08698165858771370987"

class EasyCaloriesApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val trackerParams = MyTracker.getTrackerParams()
        val trackerConfig = MyTracker.getTrackerConfig()
        MyTracker.initTracker(SDK_KEY, this)
    }
}


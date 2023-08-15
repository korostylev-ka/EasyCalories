package ru.korostylev.easycalories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.korostylev.easycalories.ui.HomeFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment == null) {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK))
            val month = (calendar.get(Calendar.MONTH) + 1)
            val year = calendar.get(Calendar.YEAR)
            val fragment = HomeFragment.newInstance(day, dayOfWeek, month, year)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
        }

    }
}
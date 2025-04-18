package ru.korostylev.easycalories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import com.google.firebase.FirebaseApp
import ru.korostylev.easycalories.ui.AboutFragment
import ru.korostylev.easycalories.ui.EditLimitsFragment
import ru.korostylev.easycalories.ui.HomeFragment
import ru.korostylev.easycalories.ui.ProfileFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editLimits -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, EditLimitsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.editProfile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ProfileFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.about -> {
                val myDialogFragment = AboutFragment()
                val manager = supportFragmentManager
                myDialogFragment.show(manager, "myDialog")
                return true
            }
            else -> return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment == null) {
            val calendar = Calendar.getInstance()
            val fragment = HomeFragment.newInstance(calendar.timeInMillis)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
        }

    }
}
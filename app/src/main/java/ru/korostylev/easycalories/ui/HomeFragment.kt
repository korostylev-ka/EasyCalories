package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentHomeBinding
import ru.korostylev.easycalories.dto.DayOfWeek
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

private const val HOME_FRAGMENT_TAG = "HomeFragment"
private const val DATE_ID = "dateId"
private const val  DAY_ID = "dayId"
private const val DAY_OF_WEEK_ID = "dayOfWeek"
private const val MONTH_ID = "monthID"
private const val YEAR_ID = "yearId"

class HomeFragment : Fragment() {
    val calendar = Calendar.getInstance()
    val viewModel: NutrientsViewModel by activityViewModels()
    var day = 0
    var dayOfWeek = 0
    var month = 0
    var year = 0
    var dayId = 0

    //получаем текущий день, месяц и год в числовом виде
    fun getCurrentDay(): Int {
        val day = day.toString()
        val month = month.toString()
        val year = year.toString()
        return (day + month + year).toInt()


    }

    fun getStringDate(): String {
        val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = day.toString()
        val month = getString(AndroidUtils.getMonth(month))
        val year = year.toString()
        return ("$dayOfWeek, $day $month $year")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(HOME_FRAGMENT_TAG, "onCreate")
        //заполняем поля из аргументов
        day = arguments!!.getInt(DAY_ID)
        dayOfWeek = arguments!!.getInt(DAY_OF_WEEK_ID)
        month = arguments!!.getInt(MONTH_ID)
        year = arguments!!.getInt(YEAR_ID)
        dayId = getCurrentDay()
        //viewModel.setId(dayId)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeFragmentBinding = FragmentHomeBinding.inflate(layoutInflater)
        Log.d(HOME_FRAGMENT_TAG, "onCreateView")

        viewModel.liveDataLimits.observe(viewLifecycleOwner, Observer {nutrients->
            nutrients.let {
                with(homeFragmentBinding) {
                    proteinValue.text = it.proteins.toString()
                    fatValue.text = it.fats.toString()
                    carbValue.text = it.carbs.toString()
                    if (it != viewModel.emptyNutrients) totalDiagram.data = Pair(it, viewModel.emptyNutrients)
                    date.text = getStringDate()
                }
            }

        })

        //кнопка назад на один день
        homeFragmentBinding.backButton.setOnClickListener {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, day - 1)
            val dayBefore = calendar.get(Calendar.DAY_OF_MONTH)
            val dayOfWeekBefore = (calendar.get(Calendar.DAY_OF_WEEK))
            val monthBefore = (calendar.get(Calendar.MONTH) + 1)
            val yearBefore = calendar.get(Calendar.YEAR)
            val fragment = newInstance(dayBefore, dayOfWeekBefore, monthBefore, yearBefore)
            val fragmentManager = parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .addToBackStack(null)
                .commit()
        }

        //кнопка вперед на один день
        homeFragmentBinding.fwdButton.setOnClickListener {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, day + 1)
            val dayAfter = calendar.get(Calendar.DAY_OF_MONTH)
            val dayOfWeekAfter = (calendar.get(Calendar.DAY_OF_WEEK))
            val monthAfter = (calendar.get(Calendar.MONTH) + 1)
            val yearAfter = calendar.get(Calendar.YEAR)
            val fragment = newInstance(dayAfter, dayOfWeekAfter, monthAfter, yearAfter)
            val fragmentManager = parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .addToBackStack(null)
                .commit()

        }

        //добавляем прием пищи
        homeFragmentBinding.addFood.setOnClickListener {
            val fragment = FoodListFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }


        return homeFragmentBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(day: Int, dayOfWeek: Int, month: Int, year: Int) = HomeFragment().apply {
            arguments = Bundle().apply {
                putInt(DAY_ID, day)
                putInt(DAY_OF_WEEK_ID, dayOfWeek)
                putInt(MONTH_ID, month)
                putInt(YEAR_ID, year)
                //putInt(DATE_ID, getCurrentDay())
            }

        }
    }
}
package ru.korostylev.easycalories.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentSelectedFoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

private const val FOOD_NAME = "foodName"


class SelectedFoodItemFragment : Fragment() {
    var calendar = Calendar.getInstance()
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDay = 0
    var dayOfWeek = 0
    var month = calendar.get(Calendar.MONTH)
    var selectedMonth = 0
    var year = calendar.get(Calendar.YEAR)
    var selectedYear = 0
    var dayId = getCurrentDay()
    var foodName = ""
    var foodItem: FoodItem? = null
    val foodViewModel: FoodViewModel by activityViewModels()
    val nutrientsViewModel: NutrientsViewModel by activityViewModels()


    fun getStringDate(): String {
        //val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = day.toString()
        val month = getString(AndroidUtils.getMonth(month + 1))
        val year = year.toString()
        return ("$day $month $year")

    }

    fun getSelectedStringDate(): String {
        //val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = selectedDay.toString()
        val month = getString(AndroidUtils.getMonth(selectedMonth + 1))
        val year = selectedYear.toString()
        return ("$day $month $year")

    }

    fun getCurrentDay(): Int {
        val day = day.toString()
        val month = month.toString()
        val year = year.toString()
        return (day + month + year).toInt()
    }

    fun getSelectDay(): Int {
        val day = selectedDay.toString()
        val month = selectedMonth.toString()
        val year = selectedYear.toString()
        return (day + month + year).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            foodName = it.getString(FOOD_NAME) ?: ""
        }

        foodItem = foodViewModel.getFoodItem(foodName)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectedFoodItemBinding.inflate(layoutInflater)

        with(binding) {
            foodNameValue.setText(foodItem!!.name)
            proteinsValue.setText(foodItem!!.proteins.toString())
            fatsValue.setText(foodItem!!.fats.toString())
            carbsValue.setText(foodItem!!.carbs.toString())
            caloriesValue.setText(foodItem!!.calories.toString())
            date.setText(getStringDate())
            calendarIcon.setOnClickListener {
                val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectedDay = dayOfMonth
                    selectedMonth = monthOfYear
                    selectedYear = year
                    dayId = getSelectDay()
                    date.setText(getSelectedStringDate())
                    println("DAY ID $dayId")
                }, year, month, day)
                datePickerDialog.show()
            }
            buttonDelete.setOnClickListener {
                foodViewModel.deleteItem(foodItem!!.name)
                parentFragmentManager.popBackStack()
            }
            buttonSave.setOnClickListener {
                val proteins = proteinsValue.text.toString()
                val proteinsFloat = proteins.toFloat()
                val fats = fatsValue.text.toString()
                val fatsFloat = fats.toFloat()
                val carbs = carbsValue.text.toString()
                val carbsFloat = carbs.toFloat()
                val calories = caloriesValue.text.toString()
                val caloriesFloat = calories.toFloat()
                val nutrients = NutrientsEntity(dayId, proteinsFloat, fatsFloat, carbsFloat, caloriesFloat)
                nutrientsViewModel.addNutrients(dayId, nutrients)
                parentFragmentManager.popBackStack()
            }
        }

        return binding.root
    }

    companion object {
        fun newInstance(foodName: String) = SelectedFoodItemFragment().apply {
            arguments = Bundle().apply {
                putString(FOOD_NAME, foodName)
            }
        }

    }
}
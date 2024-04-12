package ru.korostylev.easycalories.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentSelectedFoodItemBinding
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.EatenFoodsViewModel
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.util.*

private const val ID = "id"

class EditEatenFoodItemFragment : Fragment() {
    private var calendar = Calendar.getInstance()
    private var day = 0
    private var selectedDay = 0
    private var month = 0
    private var selectedMonth = 0
    private var year = 0
    private var selectedYear = 0
    private var hour = 0
    private var minute = 0
    private var selectedHour = 0
    private var selectedMinute = 0
    private var dayId = getCurrentDay()
    private var selectedDayId = 0
    private var currentTime = "$hour:$minute"
    private var foodName = ""
    private var eatenFoodId = 0
    private var foodItem: EatenFoodsEntity? = null
    private var weightProportion = 0.0f
    private var proteinsIn100 = 0.0f
    private var fatsIn100 = 0.0f
    private var carbsIn100 = 0.0f
    private var caloriesIn100 = 0.0f
    private val foodViewModel: FoodViewModel by activityViewModels()
    private val nutrientsViewModel: NutrientsViewModel by activityViewModels()
    private val eatenFoodsViewModel: EatenFoodsViewModel by activityViewModels()

    private fun getStringDate(): String {
        //val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = day.toString()
        val month = getString(AndroidUtils.getMonth(month + 1))
        val year = year.toString()
        return ("$day $month $year")

    }

    private fun getSelectedStringDate(): String {
        val day = selectedDay.toString()
        val month = getString(AndroidUtils.getMonth(selectedMonth + 1))
        val year = selectedYear.toString()
        return ("$day $month $year")
    }

    private fun getCurrentStringTime(): String {
        val hour = hour.toString()
        val minute = when (minute) {
            in 0..9 -> "0${minute.toString()}"
            else -> minute.toString()
        }
        return ("$hour : $minute")
    }

    private fun getSelectedStringTime(): String {
        val hour = selectedHour.toString()
        val minute = when (selectedMinute) {
            in 0..9 -> "0${selectedMinute.toString()}"
            else -> selectedMinute.toString()
        }
        return ("$hour : $minute")
    }

    private fun getCurrentDay(): Int {
        val day = "%02d".format(day)
        val month = "%02d".format(month + 1)
        val year = year.toString()
        return (year + month + day).toInt()
    }

    private fun getSelectDay(): Int {
        val day = "%02d".format(selectedDay)
        val month = "%02d".format(selectedMonth + 1)
        val year = selectedYear.toString()
        return (year + month + day).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            eatenFoodId = it.getInt(ID)
        }
        foodItem = eatenFoodsViewModel.getEatenFoodItemById(eatenFoodId)
        foodItem.let {
            proteinsIn100 = it!!.portionProteins / it.portionWeight * 100
            fatsIn100 = it.portionFats / it.portionWeight * 100
            carbsIn100 = it.portionCarbs / it.portionWeight * 100
            caloriesIn100 = it.portionCalories / it.portionWeight * 100
            calendar.timeInMillis = it.time
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            dayId = foodItem!!.dayId
            selectedDayId = foodItem!!.dayId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.editingPortion)
        val binding = FragmentSelectedFoodItemBinding.inflate(layoutInflater)
        val portionWeightValueWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    with(binding) {
                        val portionWeight = portionWeightValue.text.toString()
                        val portionFloat = portionWeight.toFloat() / 100
                        proteinsValue.text = AndroidUtils.formatFloatValuesToOneSign(proteinsIn100 * portionFloat).toString()
                        fatsValue.text = AndroidUtils.formatFloatValuesToOneSign(fatsIn100 * portionFloat).toString()
                        carbsValue.text = AndroidUtils.formatFloatValuesToOneSign(carbsIn100 * portionFloat).toString()
                        caloriesValue.text = AndroidUtils.formatFloatValuesToOneSign(caloriesIn100 * portionFloat).toString()
                    }
                } catch (e: java.lang.NumberFormatException) {
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                        .show()
                } catch (e: java.lang.IllegalArgumentException) {
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        with(binding) {
            portionWeightValue.requestFocus()
            portionWeightValue.addTextChangedListener(portionWeightValueWatcher)
            foodNameValue.text = foodItem!!.name
            portionWeightValue.setText(foodItem!!.portionWeight.toString())
            proteinsValue.text = foodItem!!.portionProteins.toString()
            fatsValue.text = foodItem!!.portionFats.toString()
            carbsValue.text = foodItem!!.portionCarbs.toString()
            caloriesValue.text = foodItem!!.portionCalories.toString()
            date.text = getStringDate()
            time.text = getCurrentStringTime()
            date.setOnClickListener {
                val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    selectedDay = dayOfMonth
                    selectedMonth = monthOfYear
                    selectedYear = year
                    selectedDayId = getSelectDay()
                    date.setText(getSelectedStringDate())
                }, year, month, day)
                datePickerDialog.show()
            }
            clockIcon.setOnClickListener {
                val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    time.setText(getSelectedStringTime())
                }, hour, minute, true)
                timePickerDialog.show()
            }
            buttonBack.setOnClickListener {
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
                val portionWeight = portionWeightValue.text.toString()
                val portionWeightFloat = portionWeight.toInt()
                val oldNutrients = NutrientsEntity(dayId, foodItem!!.portionProteins, foodItem!!.portionFats, foodItem!!.portionCarbs, foodItem!!.portionCalories)
                val changedNutrients = NutrientsEntity(selectedDayId, proteinsFloat - foodItem!!.portionProteins, fatsFloat - foodItem!!.portionFats, carbsFloat - foodItem!!.portionCarbs, caloriesFloat - foodItem!!.portionCalories)
                val newNutrients = NutrientsEntity(selectedDayId, proteinsFloat, fatsFloat, carbsFloat, caloriesFloat)
                val food = foodItem!!.copy(
                    dayId = selectedDayId,
                    time = calendar.timeInMillis,
                    portionWeight = portionWeightFloat,
                    portionProteins = proteinsFloat,
                    portionFats = fatsFloat,
                    portionCarbs = carbsFloat,
                    portionCalories = caloriesFloat
                )
                if (dayId != selectedDayId) {
                    nutrientsViewModel.removeNutrients(dayId, oldNutrients)
                    nutrientsViewModel.addNutrients(selectedDayId, newNutrients)
                    val fragment = HomeFragment.newInstance(calendar.timeInMillis)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()

                } else {
                    nutrientsViewModel.addNutrients(selectedDayId, changedNutrients)
                    val fragment = HomeFragment.newInstance(calendar.timeInMillis)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()
                }
                eatenFoodsViewModel.addEatenFood(food)

            }
        }

        return binding.root
    }

    companion object {
        fun newInstance(id: Int) = EditEatenFoodItemFragment().apply {
            arguments = Bundle().apply {
                putInt(ID, id)
            }
        }

    }
}
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
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.EatenFoodsViewModel
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.util.*
import kotlin.math.roundToInt

private const val ID = "id"

class EditEatenFoodItemFragment : Fragment() {
    var calendar = Calendar.getInstance()
    var dateInMillis: Long = calendar.timeInMillis
    var day = 0
    var selectedDay = 0
    var month = 0
    var selectedMonth = 0
    var year = 0
    var selectedYear = 0
    var hour = 0
    var minute = 0
    var selectedHour = 0
    var selectedMinute = 0
    var dayId = getCurrentDay()
    var selectedDayId = 0
//    var currentTime = getCurrentStringTime()
    var currentTime = "$hour:$minute"
    var foodName = ""
    var eatenFoodId = 0
    var foodItem: EatenFoods? = null
    var weightProportion = 0.0f
    var proteinsIn100 = 0.0f
    var fatsIn100 = 0.0f
    var carbsIn100 = 0.0f
    var caloriesIn100 = 0.0f
    val foodViewModel: FoodViewModel by activityViewModels()
    val nutrientsViewModel: NutrientsViewModel by activityViewModels()
    val eatenFoodsViewModel: EatenFoodsViewModel by activityViewModels()

    fun getStringDate(): String {
        //val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = day.toString()
        val month = getString(AndroidUtils.getMonth(month + 1))
        val year = year.toString()
        return ("$day $month $year")

    }

    fun getSelectedStringDate(): String {
        val day = selectedDay.toString()
        val month = getString(AndroidUtils.getMonth(selectedMonth + 1))
        val year = selectedYear.toString()
        return ("$day $month $year")
    }

    fun getCurrentStringTime(): String {
        val hour = hour.toString()
        val minute = when (minute) {
            in 0..9 -> "0${minute.toString()}"
            else -> minute.toString()
        }
        return ("$hour : $minute")
    }

    fun getSelectedStringTime(): String {
        val hour = selectedHour.toString()
        val minute = when (selectedMinute) {
            in 0..9 -> "0${selectedMinute.toString()}"
            else -> selectedMinute.toString()
        }
        return ("$hour : $minute")
    }

    fun getCurrentDay(): Int {
        val day = "%02d".format(day)
        val month = "%02d".format(month + 1)
        val year = year.toString()
        return (year + month + day).toInt()
    }

    fun getSelectDay(): Int {
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
//            val date = it.dayId.toString()
//            //parsing year, month, day from dayId
//            year = date.substring(0, 4).toInt()
//            month = date.substring(4, 6).toInt() - 1
//            day = date.substring(6).toInt()
//            currentTime = it.time
//            hour = currentTime.split(':')[0].trim().toInt()
//            minute = currentTime.split(':')[1].trim().toInt()
            dayId = foodItem!!.dayId
            selectedDayId = foodItem!!.dayId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            portionWeightValue.addTextChangedListener(portionWeightValueWatcher)
            foodNameValue.text = foodItem!!.name
            portionWeightValue.setText(foodItem!!.portionWeight.toString())
            val portionFloat = foodItem!!.portionWeight / 100
            proteinsValue.text = foodItem!!.portionProteins.toString()
            fatsValue.text = foodItem!!.portionFats.toString()
            carbsValue.text = foodItem!!.portionCarbs.toString()
            caloriesValue.text = foodItem!!.portionCalories.toString()
            date.text = getStringDate()
            time.text = getCurrentStringTime()

            calendarIcon.setOnClickListener {
                val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    selectedDay = dayOfMonth
                    selectedMonth = monthOfYear
                    selectedYear = year
                    selectedDayId = getSelectDay()
                    date.setText(getSelectedStringDate())
                    println("DAY Of WEEK  ${calendar.get(Calendar.DAY_OF_WEEK)}")
                }, year, month, day)
                datePickerDialog.show()
            }
            time.setOnClickListener {
                val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    time.setText(getSelectedStringTime())
                }, hour, minute, true)
                timePickerDialog.show()
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
                val portionWeight = portionWeightValue.text.toString()
                val portionWeightFloat = portionWeight.toFloat()
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
                    print("DAY OF WEEK ${calendar.get(Calendar.DAY_OF_WEEK)}")
                    print("MONTH ${calendar.get(Calendar.MONTH)}")
                    val fragment = HomeFragment.newInstance(calendar.timeInMillis)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()

                } else {
                    nutrientsViewModel.addNutrients(selectedDayId, changedNutrients)
                    print("DAY OF WEEK ${calendar.get(Calendar.DAY_OF_WEEK)}")
                    print("MONTH ${calendar.get(Calendar.MONTH)}")
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
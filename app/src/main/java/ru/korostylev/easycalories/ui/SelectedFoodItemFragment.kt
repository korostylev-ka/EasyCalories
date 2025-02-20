package ru.korostylev.easycalories.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentSelectedFoodItemBinding
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.EatenFoodsViewModel
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.util.*

class SelectedFoodItemFragment : Fragment() {
    private var _binding: FragmentSelectedFoodItemBinding? = null
    private val binding: FragmentSelectedFoodItemBinding
        get() = _binding ?: throw RuntimeException("FragmentSelectedFoodItemBinding is null")
    private var calendar = Calendar.getInstance()
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var selectedDay = EMPTY_INT_VALUE
    private var month = calendar.get(Calendar.MONTH)
    private var selectedMonth = EMPTY_INT_VALUE
    private var year = calendar.get(Calendar.YEAR)
    private var selectedYear = EMPTY_INT_VALUE
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)
    private var selectedHour = EMPTY_INT_VALUE
    private var selectedMinute = EMPTY_INT_VALUE
    private var dayId = getCurrentDay()
    private var foodName = EMPTY_STRING_VALUE
    private var foodItemEntity: FoodItemEntity? = null
    private val foodViewModel: FoodViewModel by activityViewModels()
    private val nutrientsViewModel: NutrientsViewModel by activityViewModels()
    private val eatenFoodsViewModel: EatenFoodsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            foodName = it.getString(FOOD_NAME) ?: EMPTY_STRING_VALUE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setTitle(R.string.editingPortion)
        _binding = FragmentSelectedFoodItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNutrientsValueTextWatcher()
        addClickListeners()
        getFoodItemFromDB()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun addNutrientsValueTextWatcher() {
        val nutrientsValueWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.portionWeightValue.setBackgroundResource(R.drawable.edit_text_value)

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    with(binding) {
                        val portionWeight = portionWeightValue.text.toString()
                        val portionFloat = portionWeight.toFloat() / 100
                        proteinsValue.text = (foodItemEntity!!.proteins * portionFloat).toString()
                        fatsValue.text = (foodItemEntity!!.fats * portionFloat).toString()
                        carbsValue.text = (foodItemEntity!!.carbs * portionFloat).toString()
                        caloriesValue.text = (foodItemEntity!!.calories * portionFloat).toString()
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
        binding.portionWeightValue.addTextChangedListener(nutrientsValueWatcher)
    }

    private fun addClickListeners() {
        with(binding) {
            date.setOnClickListener {
                val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    selectedDay = dayOfMonth
                    selectedMonth = monthOfYear
                    selectedYear = year
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.YEAR, year)
                    dayId = getSelectDay()
                    date.text = getSelectedStringDate()
                    println("DAY ID $dayId")
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
                val nutrients = NutrientsEntity(dayId, proteinsFloat, fatsFloat, carbsFloat, caloriesFloat)
                val food = EatenFoodsEntity(0, dayId, calendar.timeInMillis, foodName, portionWeightFloat, proteinsFloat, fatsFloat, carbsFloat, caloriesFloat)
                if (portionWeightFloat < 1F) {
                    Toast.makeText(context, R.string.portionWeightWrong, Toast.LENGTH_SHORT)
                        .show()
                    binding.portionWeightValue.requestFocus()
                    binding.portionWeightValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
                    return@setOnClickListener
                }
                nutrientsViewModel.addNutrients(dayId, nutrients)
                eatenFoodsViewModel.addEatenFood(food)
                val newFoodItemEntity = foodItemEntity!!.copy(portionWeight = portionWeightFloat, timesEaten=foodItemEntity!!.timesEaten + 1)
                foodViewModel.update(newFoodItemEntity)
                foodViewModel.getFoodList()
                parentFragmentManager.popBackStack()
                parentFragmentManager.popBackStack()
                requireActivity().setTitle(R.string.app_name)
            }
        }
    }

    private fun getFoodItemFromDB() {
        viewLifecycleOwner.lifecycleScope.launch {
            foodItemEntity = foodViewModel.getFoodItem(foodName)
            with(binding) {
                when (foodItemEntity!!.image) {
                    null -> foodImage.visibility = View.GONE
                    else -> {
                        foodImage.visibility = View.VISIBLE
                        Glide.with(foodImage)
                            .load(foodItemEntity!!.image!!.toUri())
                            .circleCrop()
                            .placeholder(R.drawable.empty_food_256dp)
                            .into(foodImage)

                    }
                }
                portionWeightValue.requestFocus()

                foodNameValue.text = foodItemEntity!!.name
                portionWeightValue.setText(foodItemEntity!!.portionWeight.toString())
                val portionFloat = foodItemEntity!!.portionWeight / 100F
                proteinsValue.text = (foodItemEntity!!.proteins * portionFloat).toString()
                fatsValue.text = (foodItemEntity!!.fats * portionFloat).toString()
                carbsValue.text = (foodItemEntity!!.carbs * portionFloat).toString()
                caloriesValue.text = (foodItemEntity!!.calories * portionFloat).toString()
                date.text = getStringDate()
                time.text = getCurrentStringTime()
            }
        }
    }

    private fun getStringDate(): String {
        val day = day.toString()
        val month = getString(AndroidUtils.getMonth(month + 1))
        val year = year.toString()
        return ("$day $month $year")    }

    private fun getSelectedStringDate(): String {
        //val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = selectedDay
        val month = getString(AndroidUtils.getMonth(selectedMonth.toInt() + 1))
        val year = selectedYear
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

    companion object {

        private const val FOOD_NAME = "foodName"
        private const val EMPTY_STRING_VALUE = ""
        private const val EMPTY_INT_VALUE = 0
        fun newInstance(foodName: String) = SelectedFoodItemFragment().apply {
            arguments = Bundle().apply {
                putString(FOOD_NAME, foodName)
            }
        }

    }
}
package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentHomeBinding
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.entity.WeightEntity
import ru.korostylev.easycalories.interfaces.OnInteractionListener
import ru.korostylev.easycalories.recyclerview.EatenFoodsListAdapter

import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.EatenFoodsViewModel
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import ru.korostylev.easycalories.viewmodel.ProfileViewModel
import java.util.Calendar
import kotlin.math.roundToInt

private const val DATE_ID = "dateId"
private const val HOME_FRAGMENT_TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val calendar = Calendar.getInstance()
    private var date: Long = calendar.timeInMillis
    private val viewModel: NutrientsViewModel by activityViewModels()
    private val foodViewModel: FoodViewModel by activityViewModels()
    private val eatenFoodsViewModel: EatenFoodsViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val listener = object: OnInteractionListener {
        override fun remove(eatenFoodsEntity: EatenFoodsEntity) {
            val foodItemEntity = lifecycleScope.launch {
                val foodItemEntity = foodViewModel.getFoodItem(eatenFoodsEntity.name)
                if (foodItemEntity != null) {
                    foodViewModel.update(foodItemEntity.copy(timesEaten = foodItemEntity.timesEaten - 1))
                }
            }
            viewModel.removeNutrients(eatenFoodsEntity)
            eatenFoodsViewModel.deleteEatenFood(eatenFoodsEntity.id)
        }

        override fun toEditFoodItemFragment(foodId: Int) {
            TODO("Not yet implemented")
        }

    }
    private var eatenFoodsAdapter: EatenFoodsListAdapter? = EatenFoodsListAdapter((emptyList()), listener)
    private var eatenFoodsRecyclerView: RecyclerView? = null
    private var day = 0
    private var dayOfWeek = 0
    private var month = 0
    private var year = 0
    private var dayId = 0
    private var weight: Float = 0F
    private var proteinsLimit = 0.0f
    private var fatsLimit = 0.0f
    private var carbsLimit = 0.0f
    private var caloriesLimit = 0.0f
    private var proteinsActual = 0.0f
    private var fatsActual = 0.0f
    private var carbsActual = 0.0f
    private var caloriesActual = 0.0f


    //get current date in digits
    private fun getCurrentDay(): Int {
        val day = "%02d".format(day)
        val month = "%02d".format(month)
        val year = year.toString()
        return (year + month + day).toInt()
    }

    private fun getStringDate(): String {
        val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = day.toString()
        val month = getString(AndroidUtils.getMonth(month))
        val year = year.toString()
        return ("$dayOfWeek, $day $month $year")
    }

    private fun nutrientsValueName(limit: Float, actual: Float): String {
        return ("${actual.roundToInt().toString()} / ${limit.roundToInt().toString()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //заполняем поля из аргументов
        date = arguments!!.getLong(DATE_ID)
        calendar.timeInMillis = date
        day = calendar.get(Calendar.DAY_OF_MONTH)
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
        dayId = getCurrentDay()
        weight = profileViewModel.getWeight(dayId) ?: 0F
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.app_name)
        val homeFragmentBinding = FragmentHomeBinding.inflate(layoutInflater)
        val floatFieldWatcher = object : TextWatcher {
            var position = 0
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                var position = 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                position = start
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.startsWith('0')) {
                    s?.delete(position, position + 1)
                    Toast.makeText(context, R.string.startFrom0, Toast.LENGTH_LONG)
                        .show()
                }
                if ((text.startsWith('.')) || (text.startsWith(','))) {
                    s?.delete(position, position + 1)
                    Toast.makeText(context, R.string.startFrom_, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        fun reloadWeight() {
            when(weight) {
                0F -> {
                    homeFragmentBinding.weightValue.visibility = View.GONE
                    homeFragmentBinding.weightTitle.setText(R.string.setWeigt)
                }
                else -> {
                    homeFragmentBinding.weightValue.visibility = View.VISIBLE
                    homeFragmentBinding.weightTitle.setText(R.string.yourWeight)
                }
            }
        }

        homeFragmentBinding.weightValue.isFocusable = false
        val weightValue = when(weight) {
            0F -> ""
            else -> weight.toString()

        }
        reloadWeight()

        homeFragmentBinding.weightValue.setText(weightValue)
        homeFragmentBinding.weightValue.addTextChangedListener(floatFieldWatcher)
        homeFragmentBinding.changeButton.setOnClickListener {
            homeFragmentBinding.weightValue.visibility = View.VISIBLE
            homeFragmentBinding.weightValue.isFocusableInTouchMode = true
            homeFragmentBinding.weightValue.requestFocus()
            homeFragmentBinding.changeButton.visibility = View.GONE
            homeFragmentBinding.saveButton.visibility = View.VISIBLE
        }
        homeFragmentBinding.saveButton.setOnClickListener {
            val newWeightValue = homeFragmentBinding.weightValue.text.toString()
            if (!newWeightValue.isEmpty()) {
                weight = newWeightValue.toFloat()
                profileViewModel.saveWeight(WeightEntity(dayId, weight))
            } else {
                weight = 0F
                profileViewModel.saveWeight(WeightEntity(dayId, weight))
            }
            homeFragmentBinding.weightValue.clearFocus()
            homeFragmentBinding.weightValue.isFocusable = false
            homeFragmentBinding.changeButton.visibility = View.VISIBLE
            homeFragmentBinding.saveButton.visibility = View.GONE
            reloadWeight()
        }
        eatenFoodsRecyclerView = homeFragmentBinding.eatenFoodsRecyclerView
        eatenFoodsRecyclerView!!.layoutManager = LinearLayoutManager(context)
        eatenFoodsRecyclerView!!.adapter = eatenFoodsAdapter
        eatenFoodsViewModel.getEatenFoodItemForDay(dayId).observe(
            viewLifecycleOwner,
            Observer {foods->
                val sortedFoods = foods.sortedByDescending {
                    it.time
                }
                sortedFoods.let {
                    eatenFoodsAdapter = EatenFoodsListAdapter(sortedFoods, listener)
                    eatenFoodsRecyclerView!!.adapter = eatenFoodsAdapter
                }
            }
        )
        viewModel.liveDataNutrients.observe(viewLifecycleOwner, Observer {listOfNutrients->
            listOfNutrients.let {
                with(homeFragmentBinding) {
                    val limits = it.filter { it.id == 0}[0]
                    val actualNutrients = viewModel.getActualEatenNutrients(dayId)
                    proteinsLimit = limits.proteins
                    fatsLimit = limits.fats
                    carbsLimit = limits.carbs
                    caloriesLimit = limits.calories
                    proteinsActual = actualNutrients.proteins
                    fatsActual = actualNutrients.fats
                    carbsActual = actualNutrients.carbs
                    caloriesActual = actualNutrients.calories
                    proteinValue.text = nutrientsValueName(proteinsLimit, proteinsActual)
                    fatValue.text = nutrientsValueName(fatsLimit, fatsActual)
                    carbValue.text = nutrientsValueName(carbsLimit, carbsActual)
                    caloryValue.text = nutrientsValueName(caloriesLimit, caloriesActual)
                    totalDiagram.data = actualNutrients
                    date.text = getStringDate()
                    if (proteinsActual.roundToInt() > proteinsLimit.roundToInt()) {
                        proteinValue.setBackgroundResource(R.drawable.proteins_value_view_overload)
                        proteinLabelOverload.visibility = View.VISIBLE
                        proteinLabelOverload.text = "+${(proteinsActual - proteinsLimit).roundToInt()}"
                    } else {
                        proteinValue.setBackgroundResource(R.drawable.proteins_value_view)
                        proteinLabelOverload.visibility = View.GONE
                    }
                    if (fatsActual.roundToInt() > fatsLimit.roundToInt()) {
                        fatValue.setBackgroundResource(R.drawable.fats_value_view_overload)
                        fatLabelOverload.visibility = View.VISIBLE
                        fatLabelOverload.text = "+${(fatsActual - fatsLimit).roundToInt()}"
                    } else {
                        fatValue.setBackgroundResource(R.drawable.fats_value_view)
                        fatLabelOverload.visibility = View.GONE
                    }
                    if (carbsActual.roundToInt() > carbsLimit.roundToInt()) {
                        carbValue.setBackgroundResource(R.drawable.carbs_value_view_overload)
                        carbsLabelOverload.visibility = View.VISIBLE
                        carbsLabelOverload.text = "+${(carbsActual - carbsLimit).roundToInt()}"
                    } else {
                        carbValue.setBackgroundResource(R.drawable.carbs_value_view)
                        carbsLabelOverload.visibility = View.GONE
                    }
                    if (caloriesActual.roundToInt() > caloriesLimit.roundToInt()) {
                        caloryValue.setBackgroundResource(R.drawable.calories_value_view_overload)
                        caloryLabelOverload.visibility = View.VISIBLE
                        caloryLabelOverload.text = "+${(caloriesActual - caloriesLimit).roundToInt()}"
                    } else {
                        caloryValue.setBackgroundResource(R.drawable.calories_value_view)
                        caloryLabelOverload.visibility = View.GONE
                    }
                }
            }

        })
        //back one day
        homeFragmentBinding.backButton.setOnClickListener {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, day - 1)
            val fragment = newInstance(calendar.timeInMillis)
            val fragmentManager = parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .addToBackStack(null)
                .commit()
        }

        //fwd one day
        homeFragmentBinding.fwdButton.setOnClickListener {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, day + 1)
            val fragment = newInstance(calendar.timeInMillis)
            val fragmentManager = parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .addToBackStack(null)
                .commit()
        }
        //add food eat
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
        fun newInstance(date: Long) = HomeFragment().apply {
            arguments = Bundle().apply {
                putLong(DATE_ID, date)
            }

        }
    }
}
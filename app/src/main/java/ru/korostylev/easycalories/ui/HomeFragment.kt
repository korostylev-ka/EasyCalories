package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentHomeBinding
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.interfaces.OnInteractionListener
import ru.korostylev.easycalories.recyclerview.EatenFoodsListAdapter

import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.EatenFoodsViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.util.Calendar
import kotlin.math.roundToInt

private const val DATE_ID = "dateId"
private const val HOME_FRAGMENT_TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val calendar = Calendar.getInstance()
    private var date: Long = calendar.timeInMillis
    private val viewModel: NutrientsViewModel by activityViewModels()
    private val eatenFoodsViewModel: EatenFoodsViewModel by activityViewModels()
    private val listener = object: OnInteractionListener {
        override fun remove(eatenFoodsEntity: EatenFoodsEntity) {
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
    private var proteinsLimit = 0.0f
    private var fatsLimit = 0.0f
    private var carbsLimit = 0.0f
    private var caloriesLimit = 0.0f
    private var proteinsActual = 0.0f
    private var fatsActual = 0.0f
    private var carbsActual = 0.0f
    private var caloriesActual = 0.0f
    private var userWeightBundle = Bundle()
    private var userWeight = userWeightBundle.getFloat(date.toString()) ?: 0F

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
        userWeight = arguments!!.getFloat(DATE_ID)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.app_name)
        val homeFragmentBinding = FragmentHomeBinding.inflate(layoutInflater)
        homeFragmentBinding.weightValue.setText(userWeight.toString())
        homeFragmentBinding.changeButton.setOnClickListener {
            homeFragmentBinding.weightValue.isFocusableInTouchMode = true
            homeFragmentBinding.weightValue.requestFocus()
            homeFragmentBinding.changeButton.visibility = View.GONE
            homeFragmentBinding.saveButton.visibility = View.VISIBLE
        }
        homeFragmentBinding.saveButton.setOnClickListener {
            userWeight = (homeFragmentBinding.weightValue.text.toString()).toFloat()
            userWeightBundle.putFloat(date.toString(), userWeight)
            homeFragmentBinding.weightValue.clearFocus()
            homeFragmentBinding.weightValue.isFocusable = false
            homeFragmentBinding.changeButton.visibility = View.VISIBLE
            homeFragmentBinding.saveButton.visibility = View.GONE

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
                    if (proteinsActual > proteinsLimit) {
                        proteinValue.setBackgroundResource(R.drawable.proteins_value_view_overload)
                        proteinLabelOverload.visibility = View.VISIBLE
                        proteinLabelOverload.text = "+${(proteinsActual - proteinsLimit).roundToInt()}"
                    } else {
                        proteinValue.setBackgroundResource(R.drawable.proteins_value_view)
                        proteinLabelOverload.visibility = View.GONE
                    }
                    if (fatsActual > fatsLimit) {
                        fatValue.setBackgroundResource(R.drawable.fats_value_view_overload)
                        fatLabelOverload.visibility = View.VISIBLE
                        fatLabelOverload.text = "+${(fatsActual - fatsLimit).roundToInt()}"
                    } else {
                        fatValue.setBackgroundResource(R.drawable.fats_value_view)
                        fatLabelOverload.visibility = View.GONE
                    }
                    if (carbsActual > carbsLimit) {
                        carbValue.setBackgroundResource(R.drawable.carbs_value_view_overload)
                        carbsLabelOverload.visibility = View.VISIBLE
                        carbsLabelOverload.text = "+${(carbsActual - carbsLimit).roundToInt()}"
                    } else {
                        carbValue.setBackgroundResource(R.drawable.carbs_value_view)
                        carbsLabelOverload.visibility = View.GONE
                    }
                    if (caloriesActual > caloriesLimit) {
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
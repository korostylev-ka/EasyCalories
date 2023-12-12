package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.util.Log
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
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.recyclerview.EatenFoodsListAdapter
import ru.korostylev.easycalories.recyclerview.OnInteractionListener
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.EatenFoodsViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.util.Calendar

private const val DATE_ID = "dateId"
private const val HOME_FRAGMENT_TAG = "HomeFragment"

class HomeFragment : Fragment() {
    val calendar = Calendar.getInstance()
    var date: Long = calendar.timeInMillis
    val viewModel: NutrientsViewModel by activityViewModels()
    val eatenFoodsViewModel: EatenFoodsViewModel by activityViewModels()
    val listener = object: OnInteractionListener {
        override fun remove(eatenFoods: EatenFoods) {
            viewModel.removeNutrients(eatenFoods)
            eatenFoodsViewModel.deleteEatenFood(eatenFoods.id)
        }

    }
    var eatenFoodsAdapter: EatenFoodsListAdapter? = EatenFoodsListAdapter((emptyList()), listener)
    var eatenFoodsRecyclerView: RecyclerView? = null
    var day = 0
    var dayOfWeek = 0
    var month = 0
    var year = 0
    var dayId = 0
    var proteinsLimit = 0.0f
    var fatsLimit = 0.0f
    var carbsLimit = 0.0f
    var caloriesLimit = 0.0f
    var proteinsActual = 0.0f
    var fatsActual = 0.0f
    var carbsActual = 0.0f
    var caloriesActual = 0.0f

    //get current date in digits
    fun getCurrentDay(): Int {
        val day = "%02d".format(day)
        val month = "%02d".format(month)
        val year = year.toString()
        return (year + month + day).toInt()
    }

    fun getStringDate(): String {
        val dayOfWeek = getString(AndroidUtils.getDayOfWeek(dayOfWeek))
        val day = day.toString()
        val month = getString(AndroidUtils.getMonth(month))
        val year = year.toString()
        return ("$dayOfWeek, $day $month $year")
    }

    fun nutrientsValueName(limit: Float, actual: Float): String {
        return ("${actual.toString()} / ${limit.toString()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(HOME_FRAGMENT_TAG, "onCreate")
        //заполняем поля из аргументов
        date = arguments!!.getLong(DATE_ID)
        calendar.timeInMillis = date
        day = calendar.get(Calendar.DAY_OF_MONTH)
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
        dayId = getCurrentDay()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeFragmentBinding = FragmentHomeBinding.inflate(layoutInflater)
        Log.d(HOME_FRAGMENT_TAG, "onCreateView")
        eatenFoodsRecyclerView = homeFragmentBinding.eatenFoodsRecyclerView
        eatenFoodsRecyclerView!!.layoutManager = LinearLayoutManager(context)
        eatenFoodsRecyclerView!!.adapter = eatenFoodsAdapter
        eatenFoodsViewModel.getEatenFoodItemForDay(dayId).observe(
            viewLifecycleOwner,
            Observer {foods->
                foods.let {
                    eatenFoodsAdapter = EatenFoodsListAdapter(foods, listener)
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
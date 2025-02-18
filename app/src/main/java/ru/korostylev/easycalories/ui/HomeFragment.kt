package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class HomeFragment : Fragment() {

    private val calendar = Calendar.getInstance()
    private var date: Long = calendar.timeInMillis
    private val viewModel: NutrientsViewModel by activityViewModels()
    private val foodViewModel: FoodViewModel by activityViewModels()
    private val eatenFoodsViewModel: EatenFoodsViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding is null")
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
    private var eatenFoodsAdapter: EatenFoodsListAdapter = EatenFoodsListAdapter((emptyList()), listener)
    private lateinit var eatenFoodsRecyclerView: RecyclerView
    private var day = EMPTY_INT_VALUE
    private var dayOfWeek = EMPTY_INT_VALUE
    private var month = EMPTY_INT_VALUE
    private var year = EMPTY_INT_VALUE
    private var dayId = EMPTY_INT_VALUE
    private var weight: Float = EMPTY_FLOAT_VALUE
    private var proteinsLimit = EMPTY_FLOAT_VALUE
    private var fatsLimit = EMPTY_FLOAT_VALUE
    private var carbsLimit = EMPTY_FLOAT_VALUE
    private var caloriesLimit = EMPTY_FLOAT_VALUE
    private var proteinsActual = EMPTY_FLOAT_VALUE
    private var fatsActual = EMPTY_FLOAT_VALUE
    private var carbsActual = EMPTY_FLOAT_VALUE
    private var caloriesActual = EMPTY_FLOAT_VALUE


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
        getDate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setTitle(R.string.app_name)
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        addWeightValueTextWatcher()
        addClickListeners()
        addObservers()
        setupRV()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDate() {
        //заполняем поля из аргументов
        date = requireArguments().getLong(DATE_ID)
        calendar.timeInMillis = date
        day = calendar.get(Calendar.DAY_OF_MONTH)
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
        dayId = getCurrentDay()
        weight = profileViewModel.getWeight(dayId) ?: 0F
    }

    private fun addWeightValueTextWatcher() {
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
        binding.weightValue.addTextChangedListener(floatFieldWatcher)
    }

    private fun reloadWeight() {
        when(weight) {
            0F -> {
                binding.weightValue.visibility = View.GONE
                binding.weightTitle.setText(R.string.setWeigt)
            }
            else -> {
                binding.weightValue.visibility = View.VISIBLE
                binding.weightTitle.setText(R.string.yourWeight)
            }
        }
    }

    private fun addClickListeners() {
        binding.changeButton.setOnClickListener {
            binding.weightValue.visibility = View.VISIBLE
            binding.weightValue.isFocusableInTouchMode = true
            binding.weightValue.requestFocus()
            binding.changeButton.visibility = View.GONE
            binding.saveButton.visibility = View.VISIBLE
        }
        binding.saveButton.setOnClickListener {
            val newWeightValue = binding.weightValue.text.toString()
            if (!newWeightValue.isEmpty()) {
                weight = newWeightValue.toFloat()
                profileViewModel.saveWeight(WeightEntity(dayId, weight))
            } else {
                weight = 0F
                profileViewModel.saveWeight(WeightEntity(dayId, weight))
            }
            binding.weightValue.clearFocus()
            binding.weightValue.isFocusable = false
            binding.changeButton.visibility = View.VISIBLE
            binding.saveButton.visibility = View.GONE
            reloadWeight()
        }
        //back one day
        binding.backButton.setOnClickListener {
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
        binding.fwdButton.setOnClickListener {
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
        binding.addFood.setOnClickListener {
            val fragment = FoodListFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun addObservers() {
        viewModel.liveDataNutrients.observe(viewLifecycleOwner, Observer {listOfNutrients->
            listOfNutrients.let {
                with(binding) {
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
    }

    private fun setupRV() {
        eatenFoodsRecyclerView = binding.eatenFoodsRecyclerView
        eatenFoodsRecyclerView.layoutManager = LinearLayoutManager(context)
        eatenFoodsRecyclerView.adapter = eatenFoodsAdapter
    }

    private fun bindViews() {
        binding.weightValue.isFocusable = false
        val weightValue = when(weight) {
            0F -> ""
            else -> weight.toString()

        }
        reloadWeight()
        binding.weightValue.setText(weightValue)
    }

    companion object {
        private const val DATE_ID = "dateId"
        private const val EMPTY_FLOAT_VALUE = 0.0f
        private const val EMPTY_INT_VALUE = 0
        @JvmStatic
        fun newInstance(date: Long) = HomeFragment().apply {
            arguments = Bundle().apply {
                putLong(DATE_ID, date)
            }

        }
    }
}
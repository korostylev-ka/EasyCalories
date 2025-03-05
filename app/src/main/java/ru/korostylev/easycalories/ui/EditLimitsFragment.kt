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
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentEditLimitsBinding
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.utils.AndroidUtils.calculateCalories
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import kotlin.math.roundToInt


class EditLimitsFragment : Fragment() {
    private var proteinsLimit = EMPTY_FLOAT_VALUE
    private var proteinsPercent = EMPTY_INT_VALUE
    private var fatsLimit = EMPTY_FLOAT_VALUE
    private var fatsPercent = EMPTY_INT_VALUE
    private var carbsLimit = EMPTY_FLOAT_VALUE
    private var carbsPercent = EMPTY_INT_VALUE
    private var caloriesLimit = EMPTY_FLOAT_VALUE
    private var waterLimit = EMPTY_INT_VALUE
    private val viewModel: NutrientsViewModel by activityViewModels()
    private var _binding: FragmentEditLimitsBinding? = null
    private val binding: FragmentEditLimitsBinding
        get() = _binding ?: throw RuntimeException("FragmentEditLimitsBinding is null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel.limitsOfNutrients()) {
            proteinsLimit = this.proteins
            fatsLimit = this.fats
            carbsLimit = this.carbs
            caloriesLimit = this.calories
            if (caloriesLimit != EMPTY_FLOAT_VALUE) {
                proteinsPercent = (proteinsLimit * 4 * 100/ caloriesLimit).roundToInt()
                fatsPercent = (fatsLimit * 9 * 100 / caloriesLimit).roundToInt()
                carbsPercent = (carbsLimit * 4 * 100 / caloriesLimit).roundToInt()
            } else {
                proteinsPercent = EMPTY_INT_VALUE
                fatsPercent = EMPTY_INT_VALUE
                carbsPercent = EMPTY_INT_VALUE
            }
        }
        with(viewModel.limitOfWater()) {
            waterLimit = this.waterVolume
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditLimitsBinding.inflate(layoutInflater)
        requireActivity().setTitle(R.string.editCaloriesLimitFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        addTextWatchers()
        addClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() {
        with (binding) {
            proteinsValueAddGramm.setText(proteinsLimit.toString())
            fatsValueAddGramm.setText(fatsLimit.toString())
            carbsValueAddGramm.setText(carbsLimit.toString())
            proteinsValueAddPercent.isFocusable = false
            fatsValueAddPercent.isFocusable = false
            carbsValueAddPercent.isFocusable = false
            val summ = proteinsPercent + fatsPercent + carbsPercent
            if (summ != 100 && summ != 0) {
                val list = listOf(proteinsPercent, fatsPercent, carbsPercent)
                val max = list.max()
                for ((index, items) in list.withIndex()) {
                    if (items == max) {
                        when (index) {
                            0 -> proteinsPercent = 100 - (fatsPercent + carbsPercent)
                            1 -> fatsPercent = 100 - (proteinsPercent + carbsPercent)
                            2 -> carbsPercent = 100 - (proteinsPercent + fatsPercent)
                        }
                    }
                }
            }
            proteinsValueAddPercent.setText(proteinsPercent.toString())
            fatsValueAddPercent.setText(fatsPercent.toString())
            carbsValueAddPercent.setText(carbsPercent.toString())
            caloriesValueAdd.setText(caloriesLimit.toString())
            waterValueAdd.setText(waterLimit.toString())

        }
    }

    private fun addTextWatchers() {
        val nutrientsValueWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(sequence: Editable?) {
                with (binding) {
                    try {
                        var proteinsPercent = 0
                        var fatsPercent = 0
                        var carbsPercent = 0
                        val proteinsGramm = (proteinsValueAddGramm.text.toString())
                        val proteinsGrammFloat = proteinsGramm.toFloat()
                        val fatsGramm = (fatsValueAddGramm.text.toString())
                        val fatsGrammFloat = fatsGramm.toFloat()
                        val carbsGramm = (carbsValueAddGramm.text.toString())
                        val carbsGrammFloat = carbsGramm.toFloat()
                        val caloriesValue = calculateCalories(proteinsGrammFloat, fatsGrammFloat, carbsGrammFloat)
                        val caloriesFloat = caloriesValue.toFloat()
                        if (caloriesFloat != 0F) {
                            proteinsPercent = (proteinsGrammFloat * 4 / caloriesFloat * 100).roundToInt()
                            fatsPercent = (fatsGrammFloat * 9 / caloriesFloat * 100).roundToInt()
                            carbsPercent = (carbsGrammFloat * 4 / caloriesFloat * 100).roundToInt()
                            //check that summ of percents = 100%. If not, change max value
                            val summ = proteinsPercent + fatsPercent + carbsPercent
                            if (summ != 100) {
                                val list = listOf(proteinsPercent, fatsPercent, carbsPercent)
                                val max = list.max()
                                for ((index, items) in list.withIndex()) {
                                    if (items == max) {
                                        when (index) {
                                            0 -> proteinsPercent = 100 - (fatsPercent + carbsPercent)
                                            1 -> fatsPercent = 100 - (proteinsPercent + carbsPercent)
                                            2 -> carbsPercent = 100 - (proteinsPercent + fatsPercent)
                                        }
                                    }

                                }
                            }

                        }
                        caloriesValueAdd.setText(caloriesValue.toString())
                        proteinsValueAddPercent.setText(proteinsPercent.toString())
                        fatsValueAddPercent.setText(fatsPercent.toString())
                        carbsValueAddPercent.setText(carbsPercent.toString())
                    } catch (e: java.lang.NumberFormatException) {
                        Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                            .show()
                    } catch (e: java.lang.IllegalArgumentException) {
                        Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                            .show()
                    }

                }

            }
        }

        val numberValueWatcher = object  : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                if (input.startsWith("0")) {
                    p0?.delete(position, position + 1)
                }

            }
        }
        binding.proteinsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
        binding.fatsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
        binding.carbsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
        binding.waterValueAdd.addTextChangedListener(numberValueWatcher)
        binding.caloriesValueAdd.addTextChangedListener(numberValueWatcher)
    }

    private fun addClickListeners() {
        binding.saveButton.setOnClickListener {
            val proteins = binding.proteinsValueAddGramm.text.toString()
            proteinsLimit = proteins.toFloat()
            val fats = binding.fatsValueAddGramm.text.toString()
            fatsLimit = fats.toFloat()
            val carbs = binding.carbsValueAddGramm.text.toString()
            carbsLimit = carbs.toFloat()
            val calories = binding.caloriesValueAdd.text.toString()
            caloriesLimit = calories.toFloat()
            val water = binding.waterValueAdd.text.toString()
            waterLimit = water.toInt()
            viewModel.setLimit(NutrientsEntity(0, proteinsLimit, fatsLimit, carbsLimit, caloriesLimit))
            viewModel.setWaterLimit(waterLimit)
            requireActivity().setTitle(R.string.app_name)
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }
        binding.backButton.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }

    }

    companion object {

        private const val EMPTY_FLOAT_VALUE = 0.0f
        private const val EMPTY_INT_VALUE = 0
        fun newInstance() = EditLimitsFragment()
    }

}
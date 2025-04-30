package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentEditLimitsBinding
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.utils.AndroidUtils
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
    private var newProteinsLimit = EMPTY_FLOAT_VALUE
    private var newFatsLimit = EMPTY_FLOAT_VALUE
    private var newCarbsLimit = EMPTY_FLOAT_VALUE
    private var newCaloriesLimit = EMPTY_FLOAT_VALUE
    private var newWaterLimit = EMPTY_INT_VALUE
    private val viewModel: NutrientsViewModel by activityViewModels()
    private var _binding: FragmentEditLimitsBinding? = null
    private val binding: FragmentEditLimitsBinding
        get() = _binding ?: throw RuntimeException("FragmentEditLimitsBinding is null")
    private var nutrientsValueWatcher: TextWatcher? = null
    private var proteinPercentsValueWatcher: TextWatcher? = null
    private var fatsPercentsValueWatcher: TextWatcher? = null
    private var carbsPercentsValueWatcher: TextWatcher? = null
    private var caloriesValueWatcherFromSelf: TextWatcher? = null
    private var caloriesValueWatcherFromNutrients: TextWatcher? = null
    private var percentsWatcher = PercentWatcher.EMPTY


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
        addOnFocusChangeListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() {
        with (binding) {
            proteinsValueAddGramm.setText(String.format(null, "%.0f", proteinsLimit))
            fatsValueAddGramm.setText(String.format(null, "%.0f", fatsLimit))
            carbsValueAddGramm.setText(String.format(null, "%.0f", carbsLimit))
//            proteinsValueAddPercent.isFocusable = false
//            fatsValueAddPercent.isFocusable = false
//            carbsValueAddPercent.isFocusable = false
//            val summ = proteinsPercent + fatsPercent + carbsPercent
//            if (summ != 100 && summ != 0) {
//                val list = listOf(proteinsPercent, fatsPercent, carbsPercent)
//                val max = list.max()
//                for ((index, items) in list.withIndex()) {
//                    if (items == max) {
//                        when (index) {
//                            0 -> proteinsPercent = 100 - (fatsPercent + carbsPercent)
//                            1 -> fatsPercent = 100 - (proteinsPercent + carbsPercent)
//                            2 -> carbsPercent = 100 - (proteinsPercent + fatsPercent)
//                        }
//                    }
//                }
//            }
            proteinsValueAddPercent.setText(proteinsPercent.toString())
            fatsValueAddPercent.setText(fatsPercent.toString())
            carbsValueAddPercent.setText(carbsPercent.toString())
            caloriesValueAdd.setText(String.format(null, "%.0f", caloriesLimit))
            waterValueAdd.setText(waterLimit.toString())

        }
    }

    private fun addTextWatchers() {
        nutrientsValueWatcher = object : TextWatcher {
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
//                    val input = sequence.toString()
//                    if (input.isNullOrBlank()) {
//                        sequence?.append('0')
//                    }
                    try {
                        var proteinsPercent = 0
                        var fatsPercent = 0
                        var carbsPercent = 0
                        val proteinsGramm = (proteinsValueAddGramm.text.toString())
                        val fatsGramm = (fatsValueAddGramm.text.toString())
                        val carbsGramm = (carbsValueAddGramm.text.toString())
                        proteinsLimit = try {
                            proteinsGramm.toFloat()
                        } catch (e: RuntimeException) {
                            if (proteinsGramm == EMPTY_STRING_VALUE) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                proteinsLimit
                            }
                        }
                        fatsLimit = try {
                            fatsGramm.toFloat()
                        } catch (e: RuntimeException) {
                            if (fatsGramm == EMPTY_STRING_VALUE) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                fatsLimit
                            }
                        }
                        carbsLimit = try {
                            carbsGramm.toFloat()
                        } catch (e: RuntimeException) {
                            if (carbsGramm == EMPTY_STRING_VALUE) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                carbsLimit
                            }
                        }

                        if (proteinsValueAddGramm.text.isNotEmpty() || fatsValueAddGramm.text.isNotEmpty() || carbsValueAddGramm.text.isNotEmpty()) {
                            caloriesLimit = AndroidUtils.calculateCalories(
                                proteinsLimit,
                                fatsLimit,
                                carbsLimit
                            )
                        }
                        val caloriesFloat = caloriesLimit.toFloat()
                        if (caloriesFloat != 0F) {
                            binding.caloriesValueAdd.isFocusableInTouchMode = true
                            binding.proteinsValueAddPercent.isFocusableInTouchMode = true
                            binding.fatsValueAddPercent.isFocusableInTouchMode = true
                            binding.carbsValueAddPercent.isFocusableInTouchMode = true

                            proteinsPercent = (proteinsLimit * 4 / caloriesFloat * 100).roundToInt()
                            fatsPercent = (fatsLimit * 9 / caloriesFloat * 100).roundToInt()
                            carbsPercent = (carbsLimit * 4 / caloriesFloat * 100).roundToInt()
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

                        } else {
                            binding.caloriesValueAdd.isFocusable = false
                            binding.proteinsValueAddPercent.isFocusable = false
                            binding.fatsValueAddPercent.isFocusable = false
                            binding.carbsValueAddPercent.isFocusable = false
                        }
                        caloriesValueAdd.setText(String.format(null, "%.0f", caloriesLimit))
                        proteinsValueAddPercent.setText(proteinsPercent.toString())
                        fatsValueAddPercent.setText(fatsPercent.toString())
                        carbsValueAddPercent.setText(carbsPercent.toString())
                    } catch (e: java.lang.NumberFormatException) {
                        Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: java.lang.IllegalArgumentException) {
                        Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            }
        }

        val waterLimitValueWatcher = object  : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val inputWaterLimit = binding.waterValueAdd.text.toString()
                waterLimit = try {
                    inputWaterLimit.toInt()
                } catch (e: RuntimeException) {
                    if (inputWaterLimit == EMPTY_STRING_VALUE) {
                        EMPTY_INT_VALUE
                    } else {
                        waterLimit
                    }
                }
//                val input = p0.toString()
//                if (input.isNullOrBlank()) {
//                    p0?.append('0')
//                }
//                if (input.startsWith("0") && position == 1) {
//                    p0?.delete(0, 1)
//                    position = 0
//                }

            }
        }
        caloriesValueWatcherFromSelf = object : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                caloriesLimit = try {
                    input.toFloat()
                } catch (_: RuntimeException) {
                    EMPTY_FLOAT_VALUE
                }
                with(binding) {
                    val proteinsPercentText = proteinsValueAddPercent.text.toString()
                    val fatsPercentText = fatsValueAddPercent.text.toString()
                    val carbsPercentText = carbsValueAddPercent.text.toString()
                    val caloriesText = caloriesValueAdd.text.toString()
                    proteinsPercent = try {
                        proteinsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (proteinsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            proteinsPercent
                        }
                    }
                    fatsPercent = try {
                        fatsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (fatsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            fatsPercent
                        }
                    }
                    carbsPercent = try {
                        carbsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (carbsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            carbsPercent
                        }
                    }
                    try {
                        proteinsLimit = (proteinsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        fatsLimit= (fatsPercent * caloriesLimit / (100 * 9)).roundToInt().toFloat()
                        carbsLimit = (carbsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        proteinsValueAddGramm.setText(String.format(null, "%.0f", proteinsLimit))
                        fatsValueAddGramm.setText(String.format(null, "%.0f", fatsLimit))
                        carbsValueAddGramm.setText(String.format(null, "%.0f", carbsLimit))
                    } catch (_: RuntimeException) {

                    }


                }

            }
        }
        proteinPercentsValueWatcher = object : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                try {
                    if (input.toInt() > 100) {
                        p0?.delete(position, position + 1)
                        Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                            .show()
                    }

                } catch (_: RuntimeException) {

                }
                with(binding) {
                    val proteinsPercentText = proteinsValueAddPercent.text.toString()
                    val fatsPercentText = fatsValueAddPercent.text.toString()
                    val carbsPercentText = carbsValueAddPercent.text.toString()
                    val caloriesText = caloriesValueAdd.text.toString()
                    proteinsPercent = try {
                        proteinsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (proteinsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            proteinsPercent
                        }
                    }
                    fatsPercent = try {
                        fatsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (fatsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            fatsPercent
                        }
                    }
                    carbsPercent = try {
                        carbsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (carbsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            carbsPercent
                        }
                    }
                    val summ = proteinsPercent + fatsPercent + carbsPercent
                    if (summ != 100) {
                        Log.d("easycaloriestag", percentsWatcher.toString())
                        when (percentsWatcher) {
                            PercentWatcher.EMPTY -> {
                                if (fatsPercent > carbsPercent) {
                                    carbsPercent = (100 - proteinsPercent) / 2
                                    fatsPercent = 100 - proteinsPercent - carbsPercent
                                } else {
                                    fatsPercent = (100 - proteinsPercent) / 2
                                    carbsPercent = 100 - proteinsPercent - fatsPercent
                                }
                            }
                            PercentWatcher.PROTEINS -> {
                                if (fatsPercent > carbsPercent) {
                                    carbsPercent = (100 - proteinsPercent) / 2
                                    fatsPercent = 100 - proteinsPercent - carbsPercent
                                } else {
                                    fatsPercent = (100 - proteinsPercent) / 2
                                    carbsPercent = 100 - proteinsPercent - fatsPercent
                                }
                            }
                            PercentWatcher.FATS -> {
                                if ((proteinsPercent + fatsPercent) > 100) {
                                    p0?.delete(position, position + 1)
                                    Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                carbsPercent = 100 - proteinsPercent - fatsPercent
                            }
                            PercentWatcher.CARBS -> {
                                if ((proteinsPercent + fatsPercent) > 100) {
                                    p0?.delete(position, position + 1)
                                    Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                fatsPercent = 100 - proteinsPercent - carbsPercent
                            }
                        }

                    }
                    try {
                        proteinsLimit = (proteinsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        fatsLimit= (fatsPercent * caloriesLimit / (100 * 9)).roundToInt().toFloat()
                        carbsLimit = (carbsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        caloriesLimit = calculateCalories(proteinsLimit, fatsLimit, carbsLimit)
                        fatsValueAddPercent.setText(fatsPercent.toString())
                        carbsValueAddPercent.setText(carbsPercent.toString())
                        proteinsValueAddGramm.setText(String.format(null, "%.0f", proteinsLimit))
                        fatsValueAddGramm.setText(String.format(null, "%.0f", fatsLimit))
                        carbsValueAddGramm.setText(String.format(null, "%.0f", carbsLimit))
                        caloriesValueAdd.setText(String.format(null, "%.0f", caloriesLimit))
                    } catch (_: RuntimeException) {

                    }
                }
            }
        }
        fatsPercentsValueWatcher = object : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                try {
                    if (input.toInt() > 100) {
                        p0?.delete(position, position + 1)
                        Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                            .show()
                    }

                } catch (_: RuntimeException) {

                }
                with(binding) {
                    val proteinsPercentText = proteinsValueAddPercent.text.toString()
                    val fatsPercentText = fatsValueAddPercent.text.toString()
                    val carbsPercentText = carbsValueAddPercent.text.toString()
                    val caloriesText = caloriesValueAdd.text.toString()
                    proteinsPercent = try {
                        proteinsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (proteinsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            proteinsPercent
                        }
                    }
                    fatsPercent = try {
                        fatsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (fatsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            fatsPercent
                        }
                    }
                    carbsPercent = try {
                        carbsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (carbsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            carbsPercent
                        }
                    }
                    val summ = proteinsPercent + fatsPercent + carbsPercent
                    if (summ != 100) {
                        Log.d("easycaloriestag", percentsWatcher.toString())
                        when (percentsWatcher) {
                            PercentWatcher.EMPTY -> {
                                if (proteinsPercent > carbsPercent) {
                                    carbsPercent = (100 - fatsPercent) / 2
                                    proteinsPercent = 100 - carbsPercent - fatsPercent
                                } else {
                                    proteinsPercent = (100 - fatsPercent) / 2
                                    carbsPercent = 100 - proteinsPercent - fatsPercent
                                }
                            }
                            PercentWatcher.PROTEINS -> {
                                if ((proteinsPercent + fatsPercent) > 100) {
                                    p0?.delete(position, position + 1)
                                    Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                carbsPercent = 100 - proteinsPercent - fatsPercent
                            }
                            PercentWatcher.FATS -> {
                                if (proteinsPercent > carbsPercent) {
                                    carbsPercent = (100 - fatsPercent) / 2
                                    proteinsPercent = 100 - carbsPercent - fatsPercent
                                } else {
                                    proteinsPercent = (100 - fatsPercent) / 2
                                    carbsPercent = 100 - proteinsPercent - fatsPercent
                                }
                            }
                            PercentWatcher.CARBS -> {
                                if ((carbsPercent + fatsPercent) > 100) {
                                    p0?.delete(position, position + 1)
                                    Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                proteinsPercent = 100 - fatsPercent - carbsPercent
                            }
                        }

                    }
                    try {
                        proteinsLimit = (proteinsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        fatsLimit= (fatsPercent * caloriesLimit / (100 * 9)).roundToInt().toFloat()
                        carbsLimit = (carbsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        caloriesLimit = calculateCalories(proteinsLimit, fatsLimit, carbsLimit)
                        proteinsValueAddPercent.setText(proteinsPercent.toString())
                        carbsValueAddPercent.setText(carbsPercent.toString())
                        proteinsValueAddGramm.setText(String.format(null, "%.0f", proteinsLimit))
                        fatsValueAddGramm.setText(String.format(null, "%.0f", fatsLimit))
                        carbsValueAddGramm.setText(String.format(null, "%.0f", carbsLimit))
                        caloriesValueAdd.setText(String.format(null, "%.0f", caloriesLimit))
                    } catch (_: RuntimeException) {

                    }
                }
            }
        }
        carbsPercentsValueWatcher = object : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                try {
                    if (input.toInt() > 100) {
                        p0?.delete(position, position + 1)
                        Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                            .show()
                    }

                } catch (_: RuntimeException) {

                }
                with(binding) {
                    val proteinsPercentText = proteinsValueAddPercent.text.toString()
                    val fatsPercentText = fatsValueAddPercent.text.toString()
                    val carbsPercentText = carbsValueAddPercent.text.toString()
                    val caloriesText = caloriesValueAdd.text.toString()
                    proteinsPercent = try {
                        proteinsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (proteinsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            proteinsPercent
                        }
                    }
                    fatsPercent = try {
                        fatsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (fatsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            fatsPercent
                        }
                    }
                    carbsPercent = try {
                        carbsPercentText.toInt()
                    } catch (_: RuntimeException) {
                        if (carbsPercentText == EMPTY_STRING_VALUE) {
                            EMPTY_INT_VALUE
                        } else {
                            carbsPercent
                        }
                    }
                    val summ = proteinsPercent + fatsPercent + carbsPercent
                    if (summ != 100) {
                        Log.d("easycaloriestag", percentsWatcher.toString())
                        when (percentsWatcher) {
                            PercentWatcher.EMPTY -> {
                                if (fatsPercent > proteinsPercent) {
                                    proteinsPercent = (100 - carbsPercent) / 2
                                    fatsPercent = 100 - proteinsPercent - carbsPercent
                                } else {
                                    fatsPercent = (100 - carbsPercent) / 2
                                    proteinsPercent = 100 - carbsPercent - fatsPercent
                                }
                            }
                            PercentWatcher.PROTEINS -> {
                                if ((proteinsPercent + carbsPercent) > 100) {
                                    p0?.delete(position, position + 1)
                                    Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                fatsPercent = 100 - carbsPercent - proteinsPercent
                            }
                            PercentWatcher.FATS -> {
                                if ((carbsPercent + fatsPercent) > 100) {
                                    p0?.delete(position, position + 1)
                                    Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                proteinsPercent = 100 - fatsPercent - carbsPercent
                            }
                            PercentWatcher.CARBS -> {
                                if (fatsPercent > proteinsPercent) {
                                    proteinsPercent = (100 - carbsPercent) / 2
                                    fatsPercent = 100 - proteinsPercent - carbsPercent
                                } else {
                                    fatsPercent = (100 - carbsPercent) / 2
                                    proteinsPercent = 100 - carbsPercent - fatsPercent
                                }
                            }
                        }

                    }
                    try {
                        proteinsLimit = (proteinsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        fatsLimit= (fatsPercent * caloriesLimit / (100 * 9)).roundToInt().toFloat()
                        carbsLimit = (carbsPercent * caloriesLimit / (100 * 4)).roundToInt().toFloat()
                        caloriesLimit = calculateCalories(proteinsLimit, fatsLimit, carbsLimit)
                        proteinsValueAddPercent.setText(proteinsPercent.toString())
                        fatsValueAddPercent.setText(fatsPercent.toString())
                        proteinsValueAddGramm.setText(String.format(null, "%.0f", proteinsLimit))
                        fatsValueAddGramm.setText(String.format(null, "%.0f", fatsLimit))
                        carbsValueAddGramm.setText(String.format(null, "%.0f", carbsLimit))
                        caloriesValueAdd.setText(String.format(null, "%.0f", caloriesLimit))
                    } catch (_: RuntimeException) {

                    }
                }
            }
        }
        caloriesValueWatcherFromNutrients = object : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                caloriesLimit = try {
                    input.toFloat()
                } catch (_: RuntimeException) {
                    EMPTY_FLOAT_VALUE
                }
            }        }

        binding.waterValueAdd.addTextChangedListener(waterLimitValueWatcher)

    }

    private fun addOnFocusChangeListeners() {
        val proteinGrammOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (proteinsLimit == EMPTY_FLOAT_VALUE) {
                        binding.proteinsValueAddGramm.setText(EMPTY_STRING_VALUE)
                    }
                    binding.proteinsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
                } else {
                    binding.proteinsValueAddGramm.setText(String.format(null, "%.0f", proteinsLimit))
                    binding.proteinsValueAddGramm.removeTextChangedListener(nutrientsValueWatcher)

                }
            }

        }
        binding.proteinsValueAddGramm.onFocusChangeListener = proteinGrammOnFocusChangeListener
        val fatsGrammOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (fatsLimit == EMPTY_FLOAT_VALUE) {
                        binding.fatsValueAddGramm.setText(EMPTY_STRING_VALUE)
                    }
                    binding.fatsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
                } else {
                    binding.fatsValueAddGramm.setText(String.format(null, "%.0f", fatsLimit))
                    binding.fatsValueAddGramm.removeTextChangedListener(nutrientsValueWatcher)
                }
            }
        }
        binding.fatsValueAddGramm.onFocusChangeListener = fatsGrammOnFocusChangeListener
        val carbsGrammOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (carbsLimit == EMPTY_FLOAT_VALUE) {
                        binding.carbsValueAddGramm.setText(EMPTY_STRING_VALUE)
                    }
                    binding.carbsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
                } else {
                    binding.carbsValueAddGramm.setText(String.format(null, "%.0f", carbsLimit))
                    binding.carbsValueAddGramm.removeTextChangedListener(nutrientsValueWatcher)
                }
            }
        }
        binding.carbsValueAddGramm.onFocusChangeListener = carbsGrammOnFocusChangeListener
        val proteinPercentOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (proteinsPercent == EMPTY_INT_VALUE) {
                        binding.proteinsValueAddPercent.setText(EMPTY_STRING_VALUE)
                    }
                    binding.proteinsValueAddPercent.addTextChangedListener(proteinPercentsValueWatcher)

                } else {
                    binding.proteinsValueAddPercent.setText(proteinsPercent.toString())
                    binding.proteinsValueAddPercent.removeTextChangedListener(proteinPercentsValueWatcher)
                    percentsWatcher = PercentWatcher.PROTEINS
                }
            }

        }
        binding.proteinsValueAddPercent.onFocusChangeListener = proteinPercentOnFocusChangeListener
        val fatsPercentOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (fatsPercent == EMPTY_INT_VALUE) {
                        binding.fatsValueAddPercent.setText(EMPTY_STRING_VALUE)
                    }
                    binding.fatsValueAddPercent.addTextChangedListener(fatsPercentsValueWatcher)
                } else {
                    binding.fatsValueAddPercent.setText(fatsPercent.toString())
                    binding.fatsValueAddPercent.removeTextChangedListener(fatsPercentsValueWatcher)
                    percentsWatcher = PercentWatcher.FATS
                }
            }

        }
        binding.fatsValueAddPercent.onFocusChangeListener = fatsPercentOnFocusChangeListener
        val carbsPercentOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (carbsPercent == EMPTY_INT_VALUE) {
                        binding.carbsValueAddPercent.setText(EMPTY_STRING_VALUE)
                    }
                    binding.carbsValueAddPercent.addTextChangedListener(carbsPercentsValueWatcher)
                } else {
                    binding.carbsValueAddPercent.setText(carbsPercent.toString())
                    binding.carbsValueAddPercent.removeTextChangedListener(carbsPercentsValueWatcher)
                    percentsWatcher = PercentWatcher.CARBS
                }
            }

        }
        binding.carbsValueAddPercent.onFocusChangeListener = carbsPercentOnFocusChangeListener
        val caloriesOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (caloriesLimit == EMPTY_FLOAT_VALUE) {
                        binding.caloriesValueAdd.setText(EMPTY_STRING_VALUE)
                    }
                    binding.caloriesValueAdd.addTextChangedListener(caloriesValueWatcherFromSelf)

                } else {
                    if (caloriesLimit == EMPTY_FLOAT_VALUE) {
                        binding.caloriesValueAdd.setText(String.format(null, "%.0f", caloriesLimit))
                    } else {
                        binding.caloriesValueAdd.setText(String.format(null, "%.0f", caloriesLimit))
                    }
                    binding.caloriesValueAdd.removeTextChangedListener(caloriesValueWatcherFromSelf)

                }
            }

        }
        binding.caloriesValueAdd.onFocusChangeListener = caloriesOnFocusChangeListener
        val waterOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (waterLimit == EMPTY_INT_VALUE) {
                        binding.waterValueAdd.setText(EMPTY_STRING_VALUE)
                    }
                } else {
                    binding.waterValueAdd.setText(waterLimit.toString())
                }
            }

        }
        binding.waterValueAdd.onFocusChangeListener = waterOnFocusChangeListener
    }

    private fun addClickListeners() {
        binding.saveButton.setOnClickListener {
            try {
                val proteins = binding.proteinsValueAddGramm.text.toString()
                newProteinsLimit = proteins.toFloat()
                val fats = binding.fatsValueAddGramm.text.toString()
                newFatsLimit = fats.toFloat()
                val carbs = binding.carbsValueAddGramm.text.toString()
                newCarbsLimit = carbs.toFloat()
                val calories = binding.caloriesValueAdd.text.toString()
                newCaloriesLimit = calories.toFloat()
                val water = binding.waterValueAdd.text.toString()
                newWaterLimit = water.toInt()
                viewModel.setLimit(NutrientsEntity(
                    0,
                    newProteinsLimit,
                    newFatsLimit,
                    newCarbsLimit,
                    newCaloriesLimit
                ))
                viewModel.setWaterLimit(newWaterLimit)
                requireActivity().setTitle(R.string.app_name)
                val fm = requireActivity().supportFragmentManager
                fm.popBackStack()
            } catch (e: RuntimeException) {
                Toast.makeText(context, R.string.checkTheFieldsAreCorrect, Toast.LENGTH_SHORT)
                    .show()
                with(binding) {
                    proteinsValueAddGramm.setText(proteinsLimit.toString())
                    fatsValueAddGramm.setText(fatsLimit.toString())
                    carbsValueAddGramm.setText(carbsLimit.toString())
                    caloriesValueAdd.setText(caloriesLimit.toString())
                    waterValueAdd.setText(waterLimit.toString())
                }
            }

        }
        binding.backButton.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }

    }

    companion object {

        private const val EMPTY_FLOAT_VALUE = 0.0f
        private const val EMPTY_INT_VALUE = 0
        private const val EMPTY_STRING_VALUE = ""
        fun newInstance() = EditLimitsFragment()
    }

    enum class PercentWatcher {
        EMPTY, PROTEINS, FATS, CARBS;
    }

}
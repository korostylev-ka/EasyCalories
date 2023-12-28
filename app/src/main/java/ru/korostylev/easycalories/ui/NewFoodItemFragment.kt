package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentNewFoodItemBinding

import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import java.util.*


class NewFoodItemFragment : Fragment() {
    private val foodViewModel: FoodViewModel by activityViewModels()
    private var foodId = 0
    private var categoryId = 0
    private var name = ""
    private var glycemicIndex = 0
    private var portionWeight = 100
    private var proteins = 0F
    private var fats = 0F
    private var carbs = 0F
    private var calories = 0F
    private var image: String? = null
    private var barcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("fragments", "newfood oncreateview")
        requireActivity().setTitle(R.string.addingFood)
        val binding = FragmentNewFoodItemBinding.inflate(layoutInflater)
        with(binding) {
            proteinsValue.setText(proteins.toString())
            fatsValue.setText(fats.toString())
            carbsValue.setText(carbs.toString())
            portionWeightValue.setText(portionWeight.toString())
            glycemicIndexValue.setText(glycemicIndex.toString())
        }

        val nameFieldTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.foodNameValue.setBackgroundResource(R.drawable.edit_text_value)

            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        val caloriesValueWatcher = object: TextWatcher {
            var position = 0
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                position = start

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    with(binding) {
                        val proteinsString = proteinsValue.text.toString()
                        val fatsString = fatsValue.text.toString()
                        val carbsString = carbsValue.text.toString()
                        proteins = proteinsString.toFloat()
                        fats = fatsString.toFloat()
                        carbs = carbsString.toFloat()
                        if ((proteins > 100F) || (fats > 100F) || (carbs > 100F)) {
                            s?.delete(position, position + 1)
                            Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_LONG)
                                .show()
                        }
                        calories = AndroidUtils.calculateCalories(
                            proteins,
                            fats,
                            carbs
                        )
                        caloriesValue.setText(calories.toString())
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
        binding.foodNameValue.addTextChangedListener(nameFieldTextWatcher)
        binding.proteinsValue.addTextChangedListener(caloriesValueWatcher)
        binding.fatsValue.addTextChangedListener(caloriesValueWatcher)
        binding.carbsValue.addTextChangedListener(caloriesValueWatcher)
        binding.buttonSave.setOnClickListener {
            with(binding) {
                foodNameValue.setBackgroundResource(R.drawable.edit_text_value)
                val name = foodNameValue.text.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                if (name.isEmpty()) {
                    foodNameValue.requestFocus()
                    foodNameValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
                    Toast.makeText(context, R.string.tooShort, Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }
                val glycemicIndexString = glycemicIndexValue.text.toString()
                val portionWeightString = portionWeightValue.text.toString()
                val proteinsString = proteinsValue.text.toString()
                val fatsString = fatsValue.text.toString()
                val carbsString = carbsValue.text.toString()
                val caloriesString = caloriesValue.text.toString()
                try {
                    proteins = proteinsString.toFloat()
                    fats = fatsString.toFloat()
                    carbs = carbsString.toFloat()
                    val proteinsToAdd = AndroidUtils.formatFloatValues(proteins)
                    val fatsToAdd = AndroidUtils.formatFloatValues(fats)
                    val carbsToAdd = AndroidUtils.formatFloatValues(carbs)
                    val caloriesToAdd = AndroidUtils.formatFloatValues(calories)
                    glycemicIndex = glycemicIndexString.toInt()
                    if ((proteins + fats + carbs) > 100) {
                        Toast.makeText(context, R.string.summOfNutrientsMoreThan100, Toast.LENGTH_LONG)
                            .show()
                        return@setOnClickListener
                    }
                    if (proteins >= 0F && fats >=0F && carbs >= 0F ) {
                        val isFoodExist = foodViewModel.getFoodItem(name)
                        if (isFoodExist == null) {
                            val newFoodEntity = FoodItemEntity(0, foodId, categoryId, name, glycemicIndex, portionWeight, proteinsToAdd, fatsToAdd, carbsToAdd, caloriesToAdd, image, barcode, true)
                            foodViewModel.addItem(newFoodEntity)
                            parentFragmentManager.popBackStack()
                            try {
                                val key = foodViewModel.saveToFirebase(newFoodEntity.toFoodItem())
                                val food = foodViewModel.getFoodItem(name)
                                Log.d("fooditem", food.toString())
                                foodViewModel.update(food!!.copy(key = key))
                            } catch (e: ru.korostylev.easycalories.error.Error) {
                                throw e
                            }

                        } else {
                            binding.foodNameValue.requestFocus()
                            binding.foodNameValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
                            Toast.makeText(context, R.string.foodAlreadyExists, Toast.LENGTH_SHORT)
                                .show()
                        }

                    } else {
                        Toast.makeText(context, R.string.checkTheFieldsAreCorrect, Toast.LENGTH_LONG)
                            .show()
                    }
                } catch (e: java.lang.NumberFormatException) {
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        binding.buttonBack.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }

        return binding.root
    }


    companion object {
        fun newInstance() =
            NewFoodItemFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
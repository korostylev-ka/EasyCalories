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
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentNewFoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.FoodViewModel


class NewFoodItemFragment : Fragment() {
    val foodViewModel: FoodViewModel by activityViewModels()

    val emptyValue = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewFoodItemBinding.inflate(layoutInflater)
        with(binding) {
            proteinsValue.setText(emptyValue.toString())
            fatsValue.setText(emptyValue.toString())
            carbsValue.setText(emptyValue.toString())
            portionWeightValue.setText(emptyValue.toString())
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    with(binding) {
                        val proteins = proteinsValue.text.toString()
                        val fats = fatsValue.text.toString()
                        val carbs = carbsValue.text.toString()
                        val proteinsFloat = proteins.toFloat()
                        val fatsFloat = fats.toFloat()
                        val carbsFloat = carbs.toFloat()
                        val caloriesFloat = AndroidUtils.calculateCalories(
                            proteinsFloat,
                            fatsFloat,
                            carbsFloat
                        )
                        caloriesValue.setText(caloriesFloat.toString())
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
                if (name.isEmpty()) {
                    foodNameValue.requestFocus()
                    foodNameValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
                    Toast.makeText(context, R.string.tooShort, Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }
                val portionWeight = portionWeightValue.text.toString()
                val proteins = proteinsValue.text.toString()
                val fats = fatsValue.text.toString()
                val carbs = carbsValue.text.toString()
                val calories = caloriesValue.text.toString()
                try {
                    val proteinsFloat = proteins.toFloat()
                    val fatsFloat = fats.toFloat()
                    val carbsFloat = carbs.toFloat()
                    if (proteinsFloat >= 0F && fatsFloat >=0F && carbsFloat >= 0F ) {
                        val newFood = FoodItem(0, name, portionWeight.toFloat(), proteins.toFloat(), fats.toFloat(), carbs.toFloat(), calories.toFloat())
                        foodViewModel.addItem(newFood)
                        parentFragmentManager.popBackStack()
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
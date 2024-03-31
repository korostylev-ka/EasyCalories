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
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentEditFoodItemBinding
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import java.util.*

private const val FOOD_ID = "FOOD_ID"
class EditFoodItemFragment : Fragment() {
    private val foodViewModel: FoodViewModel by activityViewModels()
    private var foodItemEntity: FoodItemEntity? = null
    private var id = 0
    private var foodId = 0
    private var itemCategoryId = 0
    private var itemName = ""
    private var itemGlycemicIndex = 0
    private var itemPortionWeight = 100
    private var itemProteins = 0F
    private var itemFats = 0F
    private var itemCarbs = 0F
    private var itemCalories = 0F
    private var itemImage: String? = null
    private var itemBarcode: String? = null
    private var itemKey: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(FOOD_ID)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.editingFood)
        val binding = FragmentEditFoodItemBinding.inflate(layoutInflater)
        val nameFieldTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editedFoodNameValue.setBackgroundResource(R.drawable.edit_text_value)
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
                        val proteinsString = editedProteinsValue.text.toString()
                        val fatsString = editedFatsValue.text.toString()
                        val carbsString = editedCarbsValue.text.toString()
                        itemProteins = proteinsString.toFloat()
                        itemFats = fatsString.toFloat()
                        itemCarbs = carbsString.toFloat()
                        if ((itemProteins > 100F) || (itemFats > 100F) || (itemCarbs > 100F)) {
                            s?.delete(position, position + 1)
                            Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_LONG)
                                .show()
                        }
                        itemCalories = AndroidUtils.calculateCalories(
                            itemProteins,
                            itemFats,
                            itemCarbs
                        )
                        editedCaloriesValue.setText(itemCalories.toString())
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


        viewLifecycleOwner.lifecycleScope.launch {
            foodItemEntity = foodViewModel.getFoodItemById(id)
            with(foodItemEntity) {
                foodId = this!!.foodId
                itemCategoryId = this!!.categoryId
                itemName = this!!.name
                itemProteins = this!!.proteins
                itemFats = this.fats
                itemCarbs = this!!.carbs
                itemCalories = this!!.calories
                itemPortionWeight = this!!.portionWeight
                itemGlycemicIndex = this!!.glycemicIndex
                itemImage = this!!.image
                itemBarcode = this!!.barcode
                itemKey = this.key
            }
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
                editedFoodNameValue.setText(itemName)
                editedFoodNameValue.isFocusable = false
                editedProteinsValue.setText(itemProteins.toString())
                editedFatsValue.setText(itemFats.toString())
                editedCarbsValue.setText(itemCarbs.toString())
                editedCaloriesValue.setText(itemCalories.toString())
                editedGlycemicIndexValue.setText(itemGlycemicIndex.toString())
                editedPortionWeightValue.setText(itemPortionWeight.toString())
            }
            binding.editedFoodNameValue.addTextChangedListener(nameFieldTextWatcher)
            binding.editedProteinsValue.addTextChangedListener(caloriesValueWatcher)
            binding.editedFatsValue.addTextChangedListener(caloriesValueWatcher)
            binding.editedCarbsValue.addTextChangedListener(caloriesValueWatcher)
        }

        binding.editedButtonSave.setOnClickListener {
            with(binding) {
                editedFoodNameValue.setBackgroundResource(R.drawable.edit_text_value)
                val name = editedFoodNameValue.text.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                if (name.isEmpty()) {
                    editedFoodNameValue.requestFocus()
                    editedFoodNameValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
                    Toast.makeText(context, R.string.tooShort, Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }
                val glycemicIndexString = editedGlycemicIndexValue.text.toString()
                val portionWeightString = editedPortionWeightValue.text.toString()
                val proteinsString = editedProteinsValue.text.toString()
                val fatsString = editedFatsValue.text.toString()
                val carbsString = editedCarbsValue.text.toString()
                val caloriesString = editedCaloriesValue.text.toString()
                try {
                    itemProteins = proteinsString.toFloat()
                    itemFats = fatsString.toFloat()
                    itemCarbs = carbsString.toFloat()
                    val proteinsToAdd = Math.round(itemProteins * 10.0F) / 10.0F
                    val fatsToAdd = Math.round(itemFats * 10.0F) / 10.0F
                    val carbsToAdd = Math.round(itemCarbs * 10.0F) / 10.0F
                    val caloriesToAdd = Math.round(itemCalories * 10.0F) / 10.0F
                    itemGlycemicIndex = glycemicIndexString.toInt()
                    if ((itemProteins + itemFats + itemCarbs) > 100) {
                        Toast.makeText(context, R.string.summOfNutrientsMoreThan100, Toast.LENGTH_LONG)
                            .show()
                        return@setOnClickListener
                    }
                    if (itemProteins >= 0F && itemFats >=0F && itemCarbs >= 0F ) {
                        val newFoodEntity = FoodItemEntity(id, foodId, itemCategoryId, name, itemGlycemicIndex, itemPortionWeight, proteinsToAdd, fatsToAdd, carbsToAdd, caloriesToAdd, itemBarcode, itemImage, true, itemKey)
//                        foodViewModel.update(newFoodEntity)
                        foodViewModel.editToAPI(foodId, newFoodEntity)
                        parentFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(context, R.string.checkTheFieldsAreCorrect, Toast.LENGTH_LONG)
                            .show()
                    }
                } catch (e: java.lang.NumberFormatException) {
                    Log.d("wrong", "IllegalArgumentException_buttonsave")
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        binding.editedButtonBack.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }

        return binding.root
    }


    companion object {
        fun newInstance(foodId: Int) =
            EditFoodItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(FOOD_ID, foodId)

                }
            }
    }
}
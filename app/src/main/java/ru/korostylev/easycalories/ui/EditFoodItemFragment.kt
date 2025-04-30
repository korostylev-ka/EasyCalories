package ru.korostylev.easycalories.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentEditFoodItemBinding
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.media.MediaUpload
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import java.util.*


class EditFoodItemFragment : Fragment() {
    private val foodViewModel: FoodViewModel by activityViewModels()
    private var _binding: FragmentEditFoodItemBinding? = null
    private val binding: FragmentEditFoodItemBinding
        get() = _binding ?: throw RuntimeException("FragmentEditFoodItemBinding is null")
    private var foodItemEntity: FoodItemEntity? = null
    private var id = DEFAULT_ID
    private var foodId = DEFAULT_FOOD_ID
    private var itemCategoryId = DEFAULT_CATEGORY_ID
    private var itemName = EMPTY_STRING_VALUE
    private var itemGlycemicIndex = EMPTY_INT_VALUE
    private var itemProteins = EMPTY_FLOAT_VALUE
    private var itemFats = EMPTY_FLOAT_VALUE
    private var itemCarbs = EMPTY_FLOAT_VALUE
    private var itemCalories = EMPTY_FLOAT_VALUE
    private var itemImage: String? = null
    private var itemBarcode: String? = null
    private var itemKey: String? = EMPTY_STRING_VALUE
    private var imageChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFoodItem()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setTitle(R.string.editingFood)
        _binding = FragmentEditFoodItemBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        bindViews()
        addClickListeners()
        addNameTextWatcher()
        addNutrientsValueTextWatcher()
        addOnFocusChangeListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun addNameTextWatcher() {
        val nameFieldTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.editedFoodNameValue.setBackgroundResource(R.drawable.edit_text_value)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        binding.editedFoodNameValue.addTextChangedListener(nameFieldTextWatcher)
    }

    private fun addNutrientsValueTextWatcher() {

        val nutrientValueWatcher = object : TextWatcher {
            var position = 0
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {



            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                position = start

            }

            override fun afterTextChanged(s: Editable?) {//
                try {
                    with(binding) {
                        val proteinsString = editedProteinsValue.text.toString()
                        val fatsString = editedFatsValue.text.toString()
                        val carbsString = editedCarbsValue.text.toString()
                        itemProteins = try {
                            proteinsString.toFloat()
                        } catch (e: RuntimeException) {
                            if (proteinsString == EMPTY_STRING_VALUE) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                itemProteins
                            }
                        }
                        itemFats = try {
                            fatsString.toFloat()
                        } catch (e: RuntimeException) {
                            if (fatsString == EMPTY_STRING_VALUE) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                itemFats
                            }
                        }
                        itemCarbs = try {
                            carbsString.toFloat()
                        } catch (e: RuntimeException) {
                            if (carbsString == EMPTY_STRING_VALUE) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                itemCarbs
                            }
                        }

                        if ((itemProteins > 100F) || (itemFats > 100F) || (itemCarbs > 100F)) {
                            s?.delete(position, position + 1)
                            Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                .show()
                        }
                        if ((itemProteins + itemFats + itemCarbs) > 100)  {
                            s?.delete(position, position + 1)
                            Toast.makeText(context, R.string.summOfNutrientsMoreThan100, Toast.LENGTH_SHORT)
                                .show()
                        }
                        if (editedProteinsValue.text.isNotEmpty() && editedFatsValue.text.isNotEmpty() && editedCarbsValue.text.isNotEmpty()) {
                            itemCalories = AndroidUtils.calculateCalories(
                                itemProteins,
                                itemFats,
                                itemCarbs
                            )
                        }
                        editedCaloriesValue.setText(String.format(null, "%.0f", itemCalories))
                    }
                } catch (e: java.lang.NumberFormatException) {
                } catch (e: java.lang.IllegalArgumentException) {
                }
            }
        }
        val caloriesValueWatcher = object : TextWatcher {
            var position = 0
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                position = start

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    with(binding) {
                        val caloriesString = editedCaloriesValue.text.toString()
                        itemCalories = try {
                            caloriesString.toFloat()
                        } catch (e: RuntimeException) {
                            if (caloriesString == EMPTY_STRING_VALUE) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                itemCalories
                            }
                        }
                    }
                } catch (e: java.lang.NumberFormatException) {//
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_SHORT)
                        .show()
                } catch (e: java.lang.IllegalArgumentException) {
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        val glycemicValueWatcher = object : TextWatcher {
            private var position = 0
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                position = p1

            }

            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                itemGlycemicIndex = try {
                    input.toInt()
                } catch (e: RuntimeException) {
                    if (input == EMPTY_STRING_VALUE) {
                        EMPTY_INT_VALUE
                    } else {
                        itemGlycemicIndex
                    }
                }
            }
        }
        binding.editedProteinsValue.addTextChangedListener(nutrientValueWatcher)
        binding.editedFatsValue.addTextChangedListener(nutrientValueWatcher)
        binding.editedCarbsValue.addTextChangedListener(nutrientValueWatcher)
        binding.editedCaloriesValue.addTextChangedListener(caloriesValueWatcher)
        binding.editedGlycemicIndexValue.addTextChangedListener(glycemicValueWatcher)

    }

    private fun addOnFocusChangeListeners() {
        val proteinOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (itemProteins == EMPTY_FLOAT_VALUE) {
                        binding.editedProteinsValue.setText(EMPTY_STRING_VALUE)
                    }
                } else {
                    if (itemProteins == EMPTY_FLOAT_VALUE) {
                        binding.editedProteinsValue.setText(String.format(null, "%.0f", itemProteins))
                    } else {
                        binding.editedProteinsValue.setText(String.format(null, "%.1f", itemProteins))
                    }
                }
            }
        }
        binding.editedProteinsValue.onFocusChangeListener = proteinOnFocusChangeListener
        val fatsOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (itemFats == EMPTY_FLOAT_VALUE) {
                        binding.editedFatsValue.setText(EMPTY_STRING_VALUE)
                    }
                } else {
                    if (itemFats == EMPTY_FLOAT_VALUE) {
                        binding.editedFatsValue.setText(String.format(null, "%.0f", itemFats))
                    } else {
                        binding.editedFatsValue.setText(String.format(null, "%.1f", itemFats))
                    }
                }
            }

        }
        binding.editedFatsValue.onFocusChangeListener = fatsOnFocusChangeListener
        val carbsOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (itemCarbs == EMPTY_FLOAT_VALUE) {
                        binding.editedCarbsValue.setText(EMPTY_STRING_VALUE)
                    }
                } else {
                    if (itemCarbs == EMPTY_FLOAT_VALUE) {
                        binding.editedCarbsValue.setText(String.format(null, "%.0f", itemCarbs))
                    } else {
                        binding.editedCarbsValue.setText(String.format(null, "%.1f", itemCarbs))
                    }
                }
            }

        }
        binding.editedCarbsValue.onFocusChangeListener = carbsOnFocusChangeListener
        val caloriesOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (itemCalories == EMPTY_FLOAT_VALUE) {
                        binding.editedCaloriesValue.setText(EMPTY_STRING_VALUE)
                    }
                } else {
                    if (itemCalories == EMPTY_FLOAT_VALUE) {
                        binding.editedCaloriesValue.setText(String.format(null, "%.0f", itemCalories))
                    } else {
                        binding.editedCaloriesValue.setText(String.format(null, "%.1f", itemCalories))
                    }
                }
            }

        }
        binding.editedCaloriesValue.onFocusChangeListener = caloriesOnFocusChangeListener
        val glicemicOnFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (itemGlycemicIndex == EMPTY_INT_VALUE) {
                        binding.editedGlycemicIndexValue.setText(EMPTY_STRING_VALUE)
                    }
                } else {
                    binding.editedGlycemicIndexValue.setText(itemGlycemicIndex.toString())
                }
            }

        }
        binding.editedGlycemicIndexValue.onFocusChangeListener = glicemicOnFocusChangeListener

    }

    private fun addClickListeners() {
        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> foodViewModel.changePhoto(it.data?.data)
                }
            }
        //make photo
        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(400)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
            imageChanged = true
        }
        binding.deletePhoto.setOnClickListener {
            foodViewModel.changePhoto(null)
        }
        //attach photo
        binding.addPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(400)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
            imageChanged = true
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
//                val glycemicIndexString = editedGlycemicIndexValue.text.toString()
//                val portionWeightString = editedPortionWeightValue.text.toString()
//                val proteinsString = editedProteinsValue.text.toString()
//                val fatsString = editedFatsValue.text.toString()
//                val carbsString = editedCarbsValue.text.toString()
//                val caloriesString = editedCaloriesValue.text.toString()
                try {
//                    itemProteins = proteinsString.toFloat()
//                    itemFats = fatsString.toFloat()
//                    itemCarbs = carbsString.toFloat()
//                    itemCalories = caloriesString.toFloat()
                    val proteinsToAdd = Math.round(itemProteins * 10.0F) / 10.0F
                    val fatsToAdd = Math.round(itemFats * 10.0F) / 10.0F
                    val carbsToAdd = Math.round(itemCarbs * 10.0F) / 10.0F
                    val caloriesToAdd = Math.round(itemCalories * 10.0F) / 10.0F
//                    itemGlycemicIndex = glycemicIndexString.toInt()
                    if ((itemProteins + itemFats + itemCarbs) > 100) {
                        Toast.makeText(
                            context,
                            R.string.summOfNutrientsMoreThan100,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return@setOnClickListener
                    }
                    if (itemProteins >= 0F && itemFats >= 0F && itemCarbs >= 0F) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            if (foodViewModel.photo.value != null && imageChanged) {
                                itemImage = try {
                                    foodViewModel.uploadPhoto(MediaUpload(foodViewModel.photo.value?.uri!!.toFile()))
                                } catch (e: Exception) {
                                    println("exception is $e")
                                    null
                                }
                            }
                            val isFoodExist = foodViewModel.getFoodItem(name)
                            if (isFoodExist == null || name == itemName) {
                                val newFoodEntity = FoodItemEntity(
                                    id = id,
                                    foodId = foodId,
                                    categoryId = itemCategoryId,
                                    name = name,
                                    glycemicIndex = itemGlycemicIndex,
                                    proteins = proteinsToAdd,
                                    fats = fatsToAdd,
                                    carbs = carbsToAdd,
                                    calories = caloriesToAdd,
                                    barcode = itemBarcode,
                                    image = itemImage,
                                    ownedByMe = true,
                                    key = itemKey
                                )
                                foodViewModel.editToAPI(foodId, newFoodEntity)
                                parentFragmentManager.popBackStack()
                            } else {
                                binding.editedFoodNameValue.requestFocus()
                                binding.editedFoodNameValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
                                Toast.makeText(
                                    context,
                                    R.string.foodAlreadyExists,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                        }

                    } else {
                        Toast.makeText(
                            context,
                            R.string.checkTheFieldsAreCorrect,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } catch (e: java.lang.NumberFormatException) {
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                        .show()
                }
            }

        }

        binding.editedButtonBack.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }
    }

    private fun getFoodItem() {
        arguments?.let {
            id = it.getInt(FOOD_ID)
        }
        foodItemEntity = foodViewModel.getFoodItemById(id)
        foodItemEntity?.let {
            foodId = it.foodId
            itemCategoryId = it.categoryId
            itemName = it.name
            itemProteins = it.proteins
            itemFats = it.fats
            itemCarbs = it.carbs
            itemCalories = it.calories
            itemGlycemicIndex = it.glycemicIndex
            itemImage = it.image
            itemBarcode = it.barcode
            itemKey = it.key
        }
    }

    private fun addObservers() {
        foodViewModel.photo.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.foodImage.visibility = View.GONE
                binding.deletePhoto.visibility = View.GONE
                return@observe
            } else {
                binding.deletePhoto.visibility = View.VISIBLE
            }

            binding.foodImage.visibility = View.VISIBLE
            Glide.with(binding.foodImage)
                .load(it.uri)
                .circleCrop()
                .placeholder(R.drawable.empty_food_256dp)
                .into(binding.foodImage)
        }
    }

    private fun bindViews() {
        with(binding) {
            editedFoodNameValue.setText(itemName)
            editedFoodNameValue.isFocusable = true
            editedProteinsValue.setText(itemProteins.toString())
            editedFatsValue.setText(itemFats.toString())
            editedCarbsValue.setText(itemCarbs.toString())
            editedCaloriesValue.setText(String.format(null, "%.0f", itemCalories))
            editedGlycemicIndexValue.setText(itemGlycemicIndex.toString())
        }

        with(binding) {
            when (foodItemEntity!!.image) {
                null -> {
                    foodViewModel.changePhoto(null)
                }
                else -> {
                    foodViewModel.changePhoto(foodItemEntity?.image?.toUri())
                }
            }

        }
    }


    companion object {

        private const val FOOD_ID = "FOOD_ID"
        private const val EMPTY_FLOAT_VALUE = 0f
        private const val EMPTY_INT_VALUE = 0
        private const val EMPTY_STRING_VALUE = ""
        private const val DEFAULT_ID = 0
        private const val DEFAULT_FOOD_ID = 0
        private const val DEFAULT_CATEGORY_ID = 0


        fun newInstance(foodId: Int) =
            EditFoodItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(FOOD_ID, foodId)

                }
            }

    }
}
package ru.korostylev.easycalories.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentNewFoodItemBinding
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.media.MediaUpload
import ru.korostylev.easycalories.utils.AndroidUtils
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import java.util.*

class NewFoodItemFragment : Fragment() {

    private var _binding: FragmentNewFoodItemBinding? = null
    private val binding: FragmentNewFoodItemBinding
        get() = _binding ?: throw RuntimeException(" FragmentNewFoodItemBinding is null")
    private val foodViewModel: FoodViewModel by activityViewModels()
    private var categoryId = EMPTY_ID
    private var glycemicIndex = EMPTY_INT_VALUE
    private var proteins = EMPTY_FLOAT_VALUE
    private var fats = EMPTY_FLOAT_VALUE
    private var carbs = EMPTY_FLOAT_VALUE
    private var calories = EMPTY_FLOAT_VALUE
    private var image: String? = null
    private var barcode: String? = null
    private val textFieldsChanged = MutableLiveData<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodViewModel.changePhoto(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setTitle(R.string.addingFood)
        _binding = FragmentNewFoodItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        addClickListeners()
        addObservers()
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
                binding.foodNameValue.setBackgroundResource(R.drawable.edit_text_value)

            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        binding.foodNameValue.addTextChangedListener(nameFieldTextWatcher)
    }

    private fun addNutrientsValueTextWatcher() {

        val nutrientValueWatcher = object : TextWatcher {
            var position = 0
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {



            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                position = start

            }

            override fun afterTextChanged(s: Editable?) {
//                val input = s.toString()
//                if (input.startsWith("0") && position == 1) {
//                    s?.delete(0, 1)
//                    position = 0
//                }
                try {
                    with(binding) {
                        val proteinsString = proteinsValue.text.toString()
                        val fatsString = fatsValue.text.toString()
                        val carbsString = carbsValue.text.toString()
                        proteins = try {
                            proteinsString.toFloat()
                        } catch (e: RuntimeException) {
                            if (proteinsString == EMPTY_TEXT) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                proteins
                            }
                        }
                        fats = try {
                            fatsString.toFloat()
                        } catch (e: RuntimeException) {
                            if (fatsString == EMPTY_TEXT) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                fats
                            }
                        }
                        carbs = try {
                            carbsString.toFloat()
                        } catch (e: RuntimeException) {
                            if (carbsString == EMPTY_TEXT) {
                                EMPTY_FLOAT_VALUE
                            } else {
                                carbs
                            }
                        }

                        if ((proteins > 100F) || (fats > 100F) || (carbs > 100F)) {
                            s?.delete(position, position + 1)
                            Toast.makeText(context, R.string.tooMuchValue, Toast.LENGTH_SHORT)
                                .show()
                        }
                        if ((proteins + fats + carbs) > 100)  {
                            s?.delete(position, position + 1)
                            Toast.makeText(context, R.string.summOfNutrientsMoreThan100, Toast.LENGTH_SHORT)
                                .show()
                        }
                        if (proteinsValue.text.isNotEmpty() || fatsValue.text.isNotEmpty() || carbsValue.text.isNotEmpty()) {
                            calories = AndroidUtils.calculateCalories(
                                proteins,
                                fats,
                                carbs
                            )
                        }
                        caloriesValue.setText(String.format(null, "%.0f", calories))
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
                        val caloriesString = caloriesValue.text.toString()
                        calories = try {
                            caloriesString.toFloat()
                        } catch (e: RuntimeException) {
                            calories
                        }
                    }
                } catch (e: java.lang.NumberFormatException) {
//                    s?.append('0')
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_SHORT)
                        .show()
                } catch (e: java.lang.IllegalArgumentException) {
                    Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_SHORT)
                        .show()
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
                glycemicIndex = try {
                    input.toInt()
                } catch (e: RuntimeException) {
                    if (input == EMPTY_TEXT) {
                        EMPTY_INT_VALUE
                    } else {
                        glycemicIndex
                    }
                }
//                if (input.isNullOrBlank()) {
//                    p0?.append('0')
//                }
//                if (input.startsWith("0") && position == 1) {
//                    p0?.delete(0, 1)
//                    position = 0
//                }

            }
        }
        binding.proteinsValue.addTextChangedListener(nutrientValueWatcher)
        binding.fatsValue.addTextChangedListener(nutrientValueWatcher)
        binding.carbsValue.addTextChangedListener(nutrientValueWatcher)
        binding.caloriesValue.addTextChangedListener(caloriesValueWatcher)
        binding.glycemicIndexValue.addTextChangedListener(numberValueWatcher)

    }

    private fun addOnFocusChangeListeners() {
        val proteinOnFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (proteins == EMPTY_FLOAT_VALUE) {
                        binding.proteinsValue.setText(EMPTY_TEXT)
                    }
                } else {
                    if (proteins == EMPTY_FLOAT_VALUE) {
                        binding.proteinsValue.setText(String.format(null, "%.0f", proteins))
                    } else {
                        binding.proteinsValue.setText(String.format(null, "%.1f", proteins))
                    }
                }
            }

        }
        binding.proteinsValue.onFocusChangeListener = proteinOnFocusChangeListener
        val fatsOnFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (fats == EMPTY_FLOAT_VALUE) {
                        binding.fatsValue.setText(EMPTY_TEXT)
                    }

                } else {
                    if (fats == EMPTY_FLOAT_VALUE) {
                        binding.fatsValue.setText(String.format(null, "%.0f", fats))
                    } else {
                        binding.fatsValue.setText(String.format(null, "%.1f", fats))
                    }
                }
            }

        }
        binding.fatsValue.onFocusChangeListener = fatsOnFocusChangeListener
        val carbsOnFocusChangeListener = object : OnFocusChangeListener {

            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (carbs == EMPTY_FLOAT_VALUE) {
                        binding.carbsValue.setText(EMPTY_TEXT)
                    }
                } else {
                    if (carbs == EMPTY_FLOAT_VALUE) {
                        binding.carbsValue.setText(String.format(null, "%.0f", carbs))
                    } else {
                        binding.carbsValue.setText(String.format(null, "%.1f", carbs))
                    }
                }
            }

        }
        binding.carbsValue.onFocusChangeListener = carbsOnFocusChangeListener
        val caloriesOnFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (calories == EMPTY_FLOAT_VALUE) {
                        binding.caloriesValue.setText(EMPTY_TEXT)
                    }
                } else {
                    if (calories == EMPTY_FLOAT_VALUE) {
                        binding.caloriesValue.setText(String.format(null, "%.0f", calories))
                    } else {
                        binding.caloriesValue.setText(String.format(null, "%.0f", calories))
                    }
                }
            }

        }
        binding.caloriesValue.onFocusChangeListener = caloriesOnFocusChangeListener
        val glicemicOnFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (glycemicIndex == EMPTY_INT_VALUE) {
                        binding.glycemicIndexValue.setText(EMPTY_TEXT)
                    }
                } else {
                    binding.glycemicIndexValue.setText(glycemicIndex.toString())
                }
            }

        }
        binding.glycemicIndexValue.onFocusChangeListener = glicemicOnFocusChangeListener

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
        }
        //make photo
        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(400)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }
        binding.deletePhoto.setOnClickListener {
            foodViewModel.changePhoto(null)
        }
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
                    calories = caloriesString.toFloat()
                    val proteinsToAdd = Math.round(proteins * 10.0F) / 10.0F
                    val fatsToAdd = Math.round(fats * 10.0F) / 10.0F
                    val carbsToAdd = Math.round(carbs * 10.0F) / 10.0F
                    val caloriesToAdd = Math.round(calories * 10.0F) / 10.0F
                    glycemicIndex = glycemicIndexString.toInt()
                    if ((proteins + fats + carbs) > 100) {
                        Toast.makeText(
                            context,
                            R.string.summOfNutrientsMoreThan100,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return@setOnClickListener
                    }
                    if (proteins >= 0F && fats >= 0F && carbs >= 0F && calories > 0F) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            if (foodViewModel.photo.value != null) {
                                image = try {
                                    foodViewModel.uploadPhoto(MediaUpload(foodViewModel.photo.value?.uri!!.toFile()))
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            val isFoodExist = foodViewModel.getFoodItem(name)
                            if (isFoodExist == null) {
                                val newFoodEntity = FoodItemEntity(
                                    id = EMPTY_ID,
                                    foodId = EMPTY_ID,
                                    categoryId = categoryId,
                                    name = name,
                                    glycemicIndex = glycemicIndex,
                                    proteins = proteinsToAdd,
                                    fats = fatsToAdd,
                                    carbs = carbsToAdd,
                                    calories = caloriesToAdd,
                                    barcode = barcode,
                                    image = image,
                                    ownedByMe = true
                                )
                                foodViewModel.saveToApi(newFoodEntity)
                                foodViewModel.changePhoto(null)
                                parentFragmentManager.popBackStack()
                            } else {
                                binding.foodNameValue.requestFocus()
                                binding.foodNameValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
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
        binding.buttonBack.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }
    }

    private fun addObservers() {
        foodViewModel.photo.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.photo.visibility = View.GONE
                binding.deletePhoto.visibility = View.GONE
                return@observe
            } else {
                binding.deletePhoto.visibility = View.VISIBLE
            }
            binding.photo.visibility = View.VISIBLE
            binding.photo.setImageURI(it.uri)
        }
    }

    private fun bindViews() {
        with(binding) {
            proteinsValue.setText(String.format(null, "%.0f", proteins))
            fatsValue.setText(String.format(null, "%.0f", fats))
            carbsValue.setText(String.format(null, "%.0f", carbs))
            caloriesValue.setText(String.format(null, "%.0f", calories))
            glycemicIndexValue.setText(glycemicIndex.toString())
        }
    }

    companion object {
        private const val EMPTY_FLOAT_VALUE = 0.0f
        private const val EMPTY_INT_VALUE = 0
        private const val EMPTY_ID = 0
        private const val EMPTY_TEXT = ""
        fun newInstance() =
            NewFoodItemFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
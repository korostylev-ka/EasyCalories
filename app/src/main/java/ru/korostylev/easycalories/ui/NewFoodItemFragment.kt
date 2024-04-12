package ru.korostylev.easycalories.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.activityViewModels
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
        foodViewModel.changePhoto(null)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.addingFood)
        val binding = FragmentNewFoodItemBinding.inflate(layoutInflater)
        with(binding) {
            proteinsValue.setText(proteins.toString())
            fatsValue.setText(fats.toString())
            carbsValue.setText(carbs.toString())
            portionWeightValue.setText(portionWeight.toString())
            glycemicIndexValue.setText(glycemicIndex.toString())
        }
        
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
                    val proteinsToAdd = Math.round(proteins * 10.0F) / 10.0F
                    val fatsToAdd = Math.round(fats * 10.0F) / 10.0F
                    val carbsToAdd = Math.round(carbs * 10.0F) / 10.0F
                    val caloriesToAdd = Math.round(calories * 10.0F) / 10.0F
                    glycemicIndex = glycemicIndexString.toInt()
                    if ((proteins + fats + carbs) > 100) {
                        Toast.makeText(context, R.string.summOfNutrientsMoreThan100, Toast.LENGTH_LONG)
                            .show()
                        return@setOnClickListener
                    }
                    if (proteins >= 0F && fats >=0F && carbs >= 0F && calories > 0F) {
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
                                val newFoodEntity = FoodItemEntity(0, 0, categoryId, name, glycemicIndex, portionWeight, proteinsToAdd, fatsToAdd, carbsToAdd, caloriesToAdd, barcode, image, true)
                                foodViewModel.saveToApi(newFoodEntity)
                                foodViewModel.changePhoto(null)
                                parentFragmentManager.popBackStack()
                            } else {
                                binding.foodNameValue.requestFocus()
                                binding.foodNameValue.setBackgroundResource(R.drawable.edit_text_value_wrong)
                                Toast.makeText(context, R.string.foodAlreadyExists, Toast.LENGTH_SHORT)
                                    .show()
                            }

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
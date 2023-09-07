package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentSelectedFoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.viewmodel.FoodViewModel

private const val FOOD_NAME = "foodName"


class SelectedFoodItemFragment : Fragment() {
    var foodName = ""
    var foodItem: FoodItem? = null
    val foodViewModel: FoodViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            foodName = it.getString(FOOD_NAME) ?: ""
        }
        foodItem = foodViewModel.foodListLiveData.value?.filter {
            it.name == foodName
        }?.get(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectedFoodItemBinding.inflate(layoutInflater)
        binding.foodName.text = foodName
        Toast.makeText(context, "$foodItem", Toast.LENGTH_LONG)
            .show()
        return binding.root
    }

    companion object {
        fun newInstance(foodName: String) = SelectedFoodItemFragment().apply {
            arguments = Bundle().apply {
                putString(FOOD_NAME, foodName)
            }
        }

    }
}
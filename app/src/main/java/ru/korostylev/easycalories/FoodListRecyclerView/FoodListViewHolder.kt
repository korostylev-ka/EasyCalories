package ru.korostylev.easycalories.FoodListRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem

class FoodListViewHolder(
    val binding: FoodItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(foodItem: FoodItem) {
        with (binding) {
            foodName.text = foodItem.name
            foodProteinsValue.text = foodItem.proteins.toString()
            foodFatsValue.text = foodItem.fats.toString()
            foodCarbsValue.text = foodItem.carbs.toString()
        }
    }


}
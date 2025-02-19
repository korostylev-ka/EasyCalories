package ru.korostylev.easycalories.recyclerview

import androidx.recyclerview.widget.DiffUtil
import ru.korostylev.easycalories.entity.FoodItemEntity

class FoodListDiffCallback: DiffUtil.ItemCallback<FoodItemEntity>() {

    override fun areItemsTheSame(oldItem: FoodItemEntity, newItem: FoodItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FoodItemEntity, newItem: FoodItemEntity): Boolean {
        return oldItem == newItem
    }
}
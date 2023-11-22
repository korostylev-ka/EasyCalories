package ru.korostylev.easycalories.recyclerview

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.EatenFoodItemBinding
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.ui.SelectedFoodItemFragment

class EatenFoodsListViewHolder(
    val binding: EatenFoodItemBinding,
    val context: Context
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    fun bind(eatenFoods: EatenFoods) {
        with (binding) {
            foodName.text = eatenFoods.name
            proteinsValue.text = eatenFoods.portionProteins.toString()
            fatsValue.text = eatenFoods.portionFats.toString()
            carbsValue.text = eatenFoods.portionCalories.toString()
        }
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO()
    }


}
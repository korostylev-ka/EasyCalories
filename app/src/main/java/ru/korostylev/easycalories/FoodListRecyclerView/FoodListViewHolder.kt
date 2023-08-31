package ru.korostylev.easycalories.FoodListRecyclerView

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.ui.SelectedFoodItemFragment

class FoodListViewHolder(
    val binding: FoodItemBinding,
    val context: Context
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    fun bind(foodItem: FoodItem) {
        with (binding) {
            foodName.text = foodItem.name
            foodProteinsValue.text = foodItem.proteins.toString()
            foodFatsValue.text = foodItem.fats.toString()
            foodCarbsValue.text = foodItem.carbs.toString()
        }
    }

    init {
        itemView.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        toSelectedFoodItemFragment()
    }

    fun toSelectedFoodItemFragment() {
        val fragment = SelectedFoodItemFragment.newInstance(binding.foodName.text.toString())
        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


}
package ru.korostylev.easycalories.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.ui.SelectedFoodItemFragment

class FoodListViewHolder(
    val binding: FoodItemBinding,
    val context: Context
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    @SuppressLint("ResourceAsColor")
    fun bind(foodItem: FoodItem) {
        with (binding) {
            foodName.text = foodItem.name
            foodProteinsValue.text = foodItem.proteins.toString()
            foodFatsValue.text = foodItem.fats.toString()
            foodCarbsValue.text = foodItem.carbs.toString()
            foodCaloriessValue.text = foodItem.calories.toString()
            when (foodItem.glycemicIndex) {
                0 -> {
                    binding.giTitle.visibility = View.GONE
                    binding.giValue.visibility = View.GONE
                }
                in 1..55 -> {
                    binding.giTitle.visibility = View.VISIBLE
                    binding.giValue.visibility = View.VISIBLE
                    binding.giValue.text = foodItem.glycemicIndex.toString()
                    binding.giValue.setTextColor(ContextCompat.getColor(context, R.color.gi_low))

                }
                in 56..69 -> {
                    binding.giTitle.visibility = View.VISIBLE
                    binding.giValue.visibility = View.VISIBLE
                    binding.giValue.text = foodItem.glycemicIndex.toString()
                    binding.giValue.setTextColor(ContextCompat.getColor(context, R.color.gi_medium))

                }
                in 70..Int.MAX_VALUE -> {
                    binding.giTitle.visibility = View.VISIBLE
                    binding.giValue.visibility = View.VISIBLE
                    binding.giValue.text = foodItem.glycemicIndex.toString()
                    binding.giValue.setTextColor(ContextCompat.getColor(context, R.color.gi_high))
                }
            }
            giValue.text = foodItem.glycemicIndex.toString()
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
            .addToBackStack(null)
            .commit()
    }


}
package ru.korostylev.easycalories.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.interfaces.APIListener
import ru.korostylev.easycalories.interfaces.FoodEntityListener
import ru.korostylev.easycalories.interfaces.OnInteractionListener
import ru.korostylev.easycalories.ui.SelectedFoodItemFragment


class FoodListViewHolder(
    val binding: FoodItemBinding,
    val context: Context,
    val apiListener: APIListener,
    val onInteractionListener: OnInteractionListener

): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    @SuppressLint("ResourceAsColor")
    fun bind(foodItemEntity: FoodItemEntity) {
        with (binding) {
            foodName.text = foodItemEntity.name
            foodProteinsValue.text = foodItemEntity.proteins.toString()
            foodFatsValue.text = foodItemEntity.fats.toString()
            foodCarbsValue.text = foodItemEntity.carbs.toString()
            foodCaloriessValue.text = foodItemEntity.calories.toString()
            when (foodItemEntity.glycemicIndex) {
                0 -> {
                    binding.giTitle.visibility = View.GONE
                    binding.giValue.visibility = View.GONE
                }
                in 1..55 -> {
                    binding.giTitle.visibility = View.VISIBLE
                    binding.giValue.visibility = View.VISIBLE
                    binding.giValue.text = foodItemEntity.glycemicIndex.toString()
                    binding.giValue.setTextColor(ContextCompat.getColor(context, R.color.gi_low))

                }
                in 56..69 -> {
                    binding.giTitle.visibility = View.VISIBLE
                    binding.giValue.visibility = View.VISIBLE
                    binding.giValue.text = foodItemEntity.glycemicIndex.toString()
                    binding.giValue.setTextColor(ContextCompat.getColor(context, R.color.gi_medium))

                }
                in 70..Int.MAX_VALUE -> {
                    binding.giTitle.visibility = View.VISIBLE
                    binding.giValue.visibility = View.VISIBLE
                    binding.giValue.text = foodItemEntity.glycemicIndex.toString()
                    binding.giValue.setTextColor(ContextCompat.getColor(context, R.color.gi_high))
                }
            }
            if (foodItemEntity.timesEaten > 0) {
                binding.foodName.setTextColor(ContextCompat.getColor(context, R.color.foodEaten))
            } else {
                binding.foodName.setTextColor(ContextCompat.getColor(context, R.color.main_color_blue))
            }
            giValue.text = foodItemEntity.glycemicIndex.toString()
            deleteButton.visibility = when(foodItemEntity.ownedByMe) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            editButton.visibility = when(foodItemEntity.ownedByMe) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            deleteButton.setOnClickListener {
                apiListener.remove(foodItemEntity.foodId)
//                foodEntityListener.delete(foodItemEntity.id)
            }
            editButton.setOnClickListener {
                onInteractionListener.toEditFoodItemFragment(foodItemEntity.id)
            }
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
package ru.korostylev.easycalories.recyclerview

import android.app.Application
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.EatenFoodItemBinding
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.ui.EditEatenFoodItemFragment
import ru.korostylev.easycalories.ui.SelectedFoodItemFragment
import ru.korostylev.easycalories.viewmodel.EatenFoodsViewModel
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import java.util.Calendar

interface OnInteractionListener {
    fun remove(eatenFoods: EatenFoods)
}

class EatenFoodsListViewHolder(
    val binding: EatenFoodItemBinding,
    val context: Context,
    val listener: OnInteractionListener
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    var id = 0
    fun bind(eatenFoods: EatenFoods) {
        val date = Calendar.getInstance()
        date.timeInMillis = eatenFoods.time
        val hour = date.get(Calendar.HOUR_OF_DAY)
        val minutes = date.get(Calendar.MINUTE)
        val time = "$hour:$minutes"
        with (binding) {
            id = eatenFoods.id
            timeOfEat.text = time
            foodName.text = eatenFoods.name
            poptionWeightValue.text = eatenFoods.portionWeight.toString()
            proteinsValue.text = eatenFoods.portionProteins.toString()
            fatsValue.text = eatenFoods.portionFats.toString()
            carbsValue.text = eatenFoods.portionCarbs.toString()
            caloriesValue.text = eatenFoods.portionCalories.toString()
            deleteButton.setOnClickListener {
                listener.remove(eatenFoods)
            }
        }
    }

    override fun onClick(v: View?) {
        toEditEatenFoodItemFragment()
    }

    fun toEditEatenFoodItemFragment() {
        val fragment = EditEatenFoodItemFragment.newInstance(id)
        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    init {
        itemView.setOnClickListener(this)
    }

}
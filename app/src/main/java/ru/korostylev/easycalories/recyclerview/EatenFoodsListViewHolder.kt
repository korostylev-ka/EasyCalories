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
import kotlin.math.roundToInt

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
        val time = getSelectedStringTime(hour, minutes)
        with (binding) {
            id = eatenFoods.id
            timeOfEat.text = time
            foodName.text = eatenFoods.name
            poptionWeightValue.text = eatenFoods.portionWeight.roundToInt().toString()
            proteinsValue.text = eatenFoods.portionProteins.roundToInt().toString()
            fatsValue.text = eatenFoods.portionFats.roundToInt().toString()
            carbsValue.text = eatenFoods.portionCarbs.roundToInt().toString()
            caloriesValue.text = eatenFoods.portionCalories.roundToInt().toString()
            deleteButton.setOnClickListener {
                listener.remove(eatenFoods)
            }
        }
    }
    private fun getSelectedStringTime(hour_: Int, minute_: Int): String {
        val hour = hour_.toString()
        val minute = when (minute_) {
            in 0..9 -> "0${minute_.toString()}"
            else -> minute_.toString()
        }
        return ("$hour : $minute")
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
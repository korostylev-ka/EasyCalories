package ru.korostylev.easycalories.recyclerview

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.EatenFoodItemBinding
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.interfaces.OnInteractionListener
import ru.korostylev.easycalories.ui.EditEatenFoodItemFragment
import java.util.Calendar
import kotlin.math.roundToInt


class EatenFoodsListViewHolder(
    val binding: EatenFoodItemBinding,
    val context: Context,
    val listener: OnInteractionListener
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    var id = 0
    fun bind(eatenFoodsEntity: EatenFoodsEntity) {
        val date = Calendar.getInstance()
        date.timeInMillis = eatenFoodsEntity.time
        val hour = date.get(Calendar.HOUR_OF_DAY)
        val minutes = date.get(Calendar.MINUTE)
        val time = getSelectedStringTime(hour, minutes)
        with (binding) {
            id = eatenFoodsEntity.id
            timeOfEat.text = time
            foodName.text = eatenFoodsEntity.name
            poptionWeightValue.text = eatenFoodsEntity.portionWeight.toString()
            proteinsValue.text = eatenFoodsEntity.portionProteins.roundToInt().toString()
            fatsValue.text = eatenFoodsEntity.portionFats.roundToInt().toString()
            carbsValue.text = eatenFoodsEntity.portionCarbs.roundToInt().toString()
            caloriesValue.text = eatenFoodsEntity.portionCalories.roundToInt().toString()
            deleteButton.setOnClickListener {
                listener.remove(eatenFoodsEntity)
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
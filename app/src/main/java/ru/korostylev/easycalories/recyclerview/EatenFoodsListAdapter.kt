package ru.korostylev.easycalories.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.databinding.EatenFoodItemBinding
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem

class EatenFoodsListAdapter(val foodList: List<EatenFoods>): RecyclerView.Adapter<EatenFoodsListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EatenFoodsListViewHolder {
        return EatenFoodsListViewHolder(EatenFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: EatenFoodsListViewHolder, position: Int) {
        val eatenFood = foodList[position]
        holder.bind(eatenFood)
    }
}
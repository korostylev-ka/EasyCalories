package ru.korostylev.easycalories.FoodListRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem

class FoodListAdapter(val foodList: List<FoodItem>): RecyclerView.Adapter<FoodListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        return FoodListViewHolder(FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.bind(foodItem)
    }
}
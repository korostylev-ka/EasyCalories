package ru.korostylev.easycalories.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.databinding.FoodItemBinding
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.interfaces.APIListener
import ru.korostylev.easycalories.interfaces.FoodEntityListener
import ru.korostylev.easycalories.interfaces.OnInteractionListener

class FoodListAdapter(val apiListener: APIListener, val onInteractionListener: OnInteractionListener): ListAdapter<FoodItemEntity, FoodListViewHolder>(FoodListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        return FoodListViewHolder(FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context, apiListener, onInteractionListener)
    }


    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        val foodItem = getItem(position)
        holder.bind(foodItem)
    }
}
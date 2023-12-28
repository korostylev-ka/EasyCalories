package ru.korostylev.easycalories.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.databinding.EatenFoodItemBinding
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.interfaces.OnInteractionListener

class EatenFoodsListAdapter(val foodList: List<EatenFoodsEntity>, val listener: OnInteractionListener): RecyclerView.Adapter<EatenFoodsListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EatenFoodsListViewHolder {
        return EatenFoodsListViewHolder(EatenFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context, listener)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: EatenFoodsListViewHolder, position: Int) {
        val eatenFood = foodList[position]
        holder.bind(eatenFood)
    }
}
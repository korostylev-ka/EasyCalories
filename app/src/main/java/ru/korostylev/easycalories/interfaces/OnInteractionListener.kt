package ru.korostylev.easycalories.interfaces

import ru.korostylev.easycalories.entity.EatenFoodsEntity

interface OnInteractionListener {
    fun remove(eatenFoodsEntity: EatenFoodsEntity)

    fun toEditFoodItemFragment(foodId: Int)
}
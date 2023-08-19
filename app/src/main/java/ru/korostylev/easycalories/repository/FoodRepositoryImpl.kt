package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.entity.FoodItem

class FoodRepositoryImpl: FoodRepository {
    private val foodList = listOf(FoodItem("Овсянка", 30F, 20F, 100F), FoodItem("Шоколад", 40F, 80F, 120F))
    override fun getFoodList(): List<FoodItem> {
        return foodList
    }

    override fun getFoodItem(id: Long): FoodItem {
        TODO("Not yet implemented")
    }

    override fun addItem() {
        TODO("Not yet implemented")
    }

    override fun deleteItem(id: Long) {
        TODO("Not yet implemented")
    }

    override fun changeItem(id: Long) {
        TODO("Not yet implemented")
    }
}
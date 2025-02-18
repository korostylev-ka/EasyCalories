package ru.korostylev.easycalories.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.dto.FoodItemFromDB

@Entity
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var foodId: Int = 0,
    var categoryId: Int = 0,
    var name: String = "",
    var glycemicIndex: Int = 0,
    var portionWeight: Int = 100,
    var proteins: Float = 0F,
    var fats: Float = 0F,
    var carbs: Float = 0F,
    var calories: Float = 0F,
    var barcode: String? = null,
    var image: String? = null,
    var ownedByMe: Boolean = false,
    var key: String? = null,
    @ColumnInfo(defaultValue = "0")
    var timesEaten: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        other as FoodItemEntity
        return (this.name == other.name) && (this.proteins == other.proteins) && (this.fats == other.fats) && (this.carbs == other.carbs) &&
                (this.calories == other.calories) && (this.glycemicIndex == other.glycemicIndex) && (this.image == other.image) &&
                (this.barcode == other.barcode)
    }

    fun toFoodItem(): FoodItem {
        return FoodItem(
            this.foodId,
            this.categoryId,
            this.name,
            this.glycemicIndex,
            this.proteins,
            this.fats,
            this.carbs,
            this.calories,
            this.barcode,
            this.image,
            this.key
        )
    }

        fun toFoodItemFromDB(): FoodItemFromDB {
            return FoodItemFromDB(
                0,
                2,
                this.categoryId,
                this.name,
                this.glycemicIndex,
                this.proteins,
                this.fats,
                this.carbs,
                this.calories,
                null,
                this.image,
                null
            )
        }


    companion object {
        fun fromFoodItem(foodItem: FoodItem): FoodItemEntity {
            return FoodItemEntity(
                0,
                foodItem.foodId,
                foodItem.categoryId,
                foodItem.name,
                foodItem.glycemicIndex,
                100,
                foodItem.proteins,
                foodItem.fats,
                foodItem.carbs,
                foodItem.calories,
                foodItem.barcode,
                foodItem.image,
                false,
                foodItem.key
            )
        }

        fun fromFoodItemFromDB(foodItemFromDB: FoodItemFromDB): FoodItemEntity {
            return FoodItemEntity(
                0,
                foodItemFromDB.id,
                foodItemFromDB.category_id,
                foodItemFromDB.name,
                foodItemFromDB.GI ?: 0,
                100,
                foodItemFromDB.proteins,
                foodItemFromDB.fats,
                foodItemFromDB.carbs,
                foodItemFromDB.calories,
                foodItemFromDB.barcode,
                foodItemFromDB.image,
                false,
                foodItemFromDB.key
            )
        }
    }
}
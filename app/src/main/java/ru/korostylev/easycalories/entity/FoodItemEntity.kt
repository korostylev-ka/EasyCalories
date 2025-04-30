package ru.korostylev.easycalories.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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

        fun toFoodItemFromDB(): FoodItemFromDB {
            return FoodItemFromDB(
                id = EMPTY_ID,
                user = USER_ID,
                category_id = this.categoryId,
                name = this.name,
                GI = this.glycemicIndex,
                proteins = this.proteins,
                fats = this.fats,
                carbs = this.carbs,
                calories = this.calories,
                barcode = null,
                image = this.image,
                key = null
            )
        }


    companion object {

        private const val USER_ID = 2
        private const val EMPTY_ID = 0

        fun fromFoodItemFromDB(foodItemFromDB: FoodItemFromDB): FoodItemEntity {
            return FoodItemEntity(
                id = 0,
                foodId = foodItemFromDB.id,
                categoryId = foodItemFromDB.category_id,
                name = foodItemFromDB.name,
                glycemicIndex = foodItemFromDB.GI ?: 0,
                proteins = foodItemFromDB.proteins,
                fats = foodItemFromDB.fats,
                carbs = foodItemFromDB.carbs,
                calories = foodItemFromDB.calories,
                barcode = foodItemFromDB.barcode,
                image = foodItemFromDB.image,
                ownedByMe = false,
                key = foodItemFromDB.key
            )
        }
    }
}
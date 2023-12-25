package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var glycemicIndex: Int = 0,
    var portionWeight: Float = 100F,
    var proteins: Float = 0F,
    var fats: Float = 0F,
    var carbs: Float = 0F,
    var calories: Float = 0F,
    var barcode: String? = null,
    var image: String? = null,
    var ownedByMe: Boolean = false,
    var key: String? = null
) {
    override fun equals(other: Any?): Boolean {
        other as FoodItem
        return (this.name == other.name) && (this.portionWeight == other.portionWeight) &&
                (this.proteins == other.proteins) && (this.fats == other.fats) && (this.carbs == other.carbs) &&
                (this.calories == other.calories) && (this.glycemicIndex == other.glycemicIndex)
    }
}
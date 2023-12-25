package ru.korostylev.easycalories.api

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.ui.FoodListFragment

class FirebaseDB {
    private val firebaseDB = FirebaseDatabase.getInstance()
    private val foodsReference = firebaseDB.getReference("foods")
    private var listOfFoodItem = mutableListOf<FoodItem>()
    private var mutableLiveDataFoods = MutableLiveData(listOfFoodItem as List<FoodItem>)
    val liveDataFoods: LiveData<List<FoodItem>> = mutableLiveDataFoods

    fun readFoodItems() {
        foodsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfFoodItem.clear()
                if (snapshot.exists()) {
                    for (foods in snapshot.children) {
                        val food = foods.getValue(FoodItem::class.java)
                        listOfFoodItem.add(food!!)
                        mutableLiveDataFoods.value = listOfFoodItem
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    fun saveFoodItem(foodItem: FoodItem): String? {
        val reference = foodsReference.push()
        val key = reference.key
        Log.d("key", key.toString())
        val newFoodItem = reference.setValue(foodItem)
        return key
    }

    fun editFoodItem(foodItem: FoodItem) {
        foodsReference.updateChildren(mapOf(foodItem.key to foodItem))

    }

    fun newInstance(): FirebaseDB {
        return FirebaseDB()
    }
}
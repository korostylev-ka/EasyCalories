package ru.korostylev.easycalories.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import ru.korostylev.easycalories.dto.FoodItem

class FirebaseDB: API {
    private val firebaseDB = FirebaseDatabase.getInstance()
    private val foodsReference = firebaseDB.getReference("foods")
    private var listOfFoodItem = mutableListOf<FoodItem>()
    private var mutableLiveDataFoods = MutableLiveData(listOfFoodItem as List<FoodItem>)
    val liveDataFoods: LiveData<List<FoodItem>> = mutableLiveDataFoods
    override fun init() {
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

    override fun getAll(): LiveData<List<FoodItem>> {
        return mutableLiveDataFoods
    }

    override fun edit(foodItem: FoodItem) {
        val reference = foodsReference.child(foodItem.key!!)
        Log.d("fire", reference.toString())
        foodsReference.updateChildren(mapOf(foodItem.key to foodItem))
    }

    override fun delete(foodItem: FoodItem) {
        val reference = foodsReference.child(foodItem.key!!)
        reference.removeValue()
    }

    override fun add(foodItem: FoodItem): String? {
        val reference = foodsReference.push()
        val key = reference.key
        val newFoodItem = reference.setValue(foodItem.copy(key= key))
        return key
    }


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


//    fun saveFoodItem(foodItem: FoodItem): String? {
//        val reference = foodsReference.push()
//        val key = reference.key
//        val newFoodItem = reference.setValue(foodItem.copy(key= key))
//        return key
//    }
//
//    fun editFoodItem(foodItem: FoodItem) {
//        val reference = foodsReference.child(foodItem.key!!)
//        Log.d("fire", reference.toString())
//        foodsReference.updateChildren(mapOf(foodItem.key to foodItem))
//
//    }
//
//    fun deleteFoodItemFromFirebase(foodItem: FoodItem) {
//        val reference = foodsReference.child(foodItem.key!!)
//        reference.removeValue()
//    }

    fun newInstance(): FirebaseDB {
        return FirebaseDB()
    }
}
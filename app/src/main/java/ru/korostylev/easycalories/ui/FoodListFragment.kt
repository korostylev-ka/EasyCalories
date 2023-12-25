package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.recyclerview.FoodListAdapter
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.databinding.FragmentFoodListBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.viewmodel.FoodViewModel
import java.util.*


class FoodListFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: FoodListAdapter? = FoodListAdapter(emptyList())
    private val viewModel: FoodViewModel by activityViewModels()
    private var foodList: List<FoodItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.chooseFood)
        val binding = FragmentFoodListBinding.inflate(layoutInflater)

        val filterTextWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val text = binding.filterText.text.toString()

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = binding.filterText.text.toString()

            }

            override fun afterTextChanged(s: Editable?) {
                val text = binding.filterText.text.toString()
                val editedList = foodList.filter {
                    it.name.startsWith(text, ignoreCase = true)
                }
                adapter = FoodListAdapter(editedList)
                recyclerView!!.adapter = adapter

            }

        }
        binding.filterText.addTextChangedListener(filterTextWatcher)
        recyclerView = binding.foodListRecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = adapter
        viewModel.foodListLiveData.observe(
            viewLifecycleOwner,
            Observer {foods->
                val sortedFoods = foods.sortedBy {
                    it.name
                }
                foodList = foods
                foodList.let {
                    adapter = FoodListAdapter(sortedFoods)
                    recyclerView!!.adapter = adapter
                }
            }
        )
        viewModel.foodListLiveDataFirebase.observe(
            viewLifecycleOwner,
            Observer {foodsFirebase->
                viewModel.updateFromFirebase(foodsFirebase)
            }
        )
        binding.addFoodItem.setOnClickListener {
            val fragment = NewFoodItemFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }

    }
}
package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.recyclerview.FoodListAdapter
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentFoodListBinding
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.interfaces.APIListener
import ru.korostylev.easycalories.interfaces.FoodEntityListener
import ru.korostylev.easycalories.interfaces.OnInteractionListener

import ru.korostylev.easycalories.viewmodel.FoodViewModel


class FoodListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodListAdapter
    private val apiListener = object: APIListener {
        override fun remove(foodId: Int) {
            viewModel.deleteByIdFromAPI(foodId)
            viewModel.getFoodList()
        }

    }
    private val onInteractionListener = object : OnInteractionListener {
        override fun remove(eatenFoodsEntity: EatenFoodsEntity) {
            TODO("Not yet implemented")
        }

        override fun toEditFoodItemFragment(foodId: Int) {
            val fragment = EditFoodItemFragment.newInstance(foodId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

    }
    private val viewModel: FoodViewModel by activityViewModels()
    private var foodList: List<FoodItemEntity> = emptyList()
    private var _binding: FragmentFoodListBinding? = null
    private val binding: FragmentFoodListBinding
        get() = _binding ?: throw RuntimeException("FragmentFoodListBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setTitle(R.string.chooseFood)
        _binding = FragmentFoodListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addObservers()
        addFilterTextWatcher()
        setupRV()
        addClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRV() {
        recyclerView = binding.foodListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun addObservers() {
        viewModel.foodListLiveData.observe(
            viewLifecycleOwner,
            Observer {foods->
                val sortedByNameFoods = foods.sortedBy {
                    it.name
                }
                val sortedByTimesEaten = sortedByNameFoods.sortedByDescending {
                    it.timesEaten
                }
                foodList = foods
                foodList.let {
                    adapter = FoodListAdapter(apiListener, onInteractionListener)
                    adapter?.submitList(sortedByTimesEaten)
                    recyclerView!!.adapter = adapter
                }
            }
        )
        viewModel.infoModel.observe(
            viewLifecycleOwner,
            Observer {
                binding.progressBar.visibility = when(it.loading) {
                    true -> View.VISIBLE
                    else -> View.GONE
                }
                if (it.isError) {
                    Toast.makeText(context, "${getString(R.string.serverError)}", Toast.LENGTH_LONG)
                        .show()
                }
                if (!it.successResponse) {
                    Toast.makeText(context," ${getString(R.string.wrongResponse)} \n ${getString(R.string.responseCode)} ${it.responseCode}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        )
    }

    private fun addFilterTextWatcher() {
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
                adapter = FoodListAdapter(apiListener, onInteractionListener)
                recyclerView.adapter = adapter
                adapter.submitList(editedList)
            }
        }
        binding.filterText.addTextChangedListener(filterTextWatcher)
    }

    private fun addClickListeners() {
        binding.addFoodItem.setOnClickListener {
            val fragment = NewFoodItemFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {

        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }

    }
}
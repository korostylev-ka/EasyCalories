package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import ru.korostylev.easycalories.databinding.FragmentFoodListBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.viewmodel.FoodViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FoodListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoodListFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapter: FoodListAdapter? = FoodListAdapter(emptyList())
    val viewModel: FoodViewModel by activityViewModels()
    var foodList: List<FoodItem> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    it.name.startsWith(text)
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
                foodList = foods
                foodList.let {
                    adapter = FoodListAdapter(foods)
                    recyclerView!!.adapter = adapter

                }

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
        /*viewModel.foodListLiveData.observe(
            viewLifecycleOwner,
            Observer {foods->
                foods.let {
                    adapter = FoodListAdapter(foods)
                    recyclerView!!.adapter = adapter
                }

            }
        )*/
    }

    companion object {
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }

    }
}
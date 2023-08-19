package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.korostylev.easycalories.FoodListRecyclerView.FoodListAdapter
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentFoodListBinding
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFoodListBinding.inflate(layoutInflater)

        recyclerView = binding.foodListRecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        adapter = FoodListAdapter(viewModel.foodList)
        recyclerView!!.adapter = adapter
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }

    }
}
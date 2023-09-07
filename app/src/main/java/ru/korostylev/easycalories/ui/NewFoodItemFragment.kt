package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentNewFoodItemBinding
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.viewmodel.FoodViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewFoodItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewFoodItemFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val foodViewModel: FoodViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewFoodItemBinding.inflate(layoutInflater)
        binding.buttonSave.setOnClickListener {
            with(binding) {
                val name = foodNameValue.text.toString()
                val portionWeight = portionWeightValue.text.toString()
                val proteins = proteinsValue.text.toString()
                val fats = fatsValue.text.toString()
                val carbs = carbsValue.text.toString()
                val calories = caloriesValue.text.toString()
                val newFood = FoodItem(0, name, proteins.toFloat(), fats.toFloat(), carbs.toFloat())
                foodViewModel.addItem(newFood)
                parentFragmentManager.popBackStack()

            }

        }
        return binding.root
    }

    companion object {
        fun newInstance() =
            NewFoodItemFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
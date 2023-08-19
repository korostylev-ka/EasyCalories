package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentEditLimitsBinding
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditLimitsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditLimitsFragment : Fragment() {

    val viewModel: NutrientsViewModel by activityViewModels()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val editLimitsBinding = FragmentEditLimitsBinding.inflate(layoutInflater)
        editLimitsBinding.saveButton.setOnClickListener {
            val proteins = editLimitsBinding.proteinsValueAddGramm.text.toString()
            val protFloat = proteins.toFloat()
            val fats = editLimitsBinding.fatsValueAddGramm.text.toString()
            val fatsFloat = fats.toFloat()
            val carbs = editLimitsBinding.carbsValueAddGramm.text.toString()
            val carbsFloat = carbs.toFloat()
            println("БЖУ $protFloat $fatsFloat $carbsFloat")
            viewModel.setLimit(NutrientsEntity(0, protFloat, fatsFloat, carbsFloat))
        }


        // Inflate the layout for this fragment
        return editLimitsBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditLimitsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditLimitsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
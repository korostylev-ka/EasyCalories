package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentEditLimitsBinding
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel

private const val PROTEINS = "proteinsLimit"
private const val FATS = "fatsLimit"
private const val CARBS = "carbsLimit"
private const val CALORIES = "caloriesLimit"
class EditLimitsFragment : Fragment() {
    var proteinsLimit = 0F
    var fatsLimit = 0F
    var carbsLimit = 0F
    var caloriesLimit = 0F

    val viewModel: NutrientsViewModel by activityViewModels()

    fun calculateCalories(proteins: String, fats: String, carbs: String): Float {
        return (proteins.toFloat() * 4 + fats.toFloat() * 9 + carbs.toFloat() * 4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("EditLimits", "onCreate")
        with(viewModel.liveDataLimits.value) {
            proteinsLimit = this?.proteins ?: 0F
            fatsLimit = this?.fats ?: 0F
            carbsLimit = this?.carbs ?: 0F
            caloriesLimit = this?.calories ?: 0F
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val editLimitsBinding = FragmentEditLimitsBinding.inflate(layoutInflater)

        Log.d("EditLimits", "onCreateView")
        requireActivity().setTitle(R.string.editCaloriesLimitFragment)
        val nutrientsValueWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                //вызываем toString для объекта CharSequence, представляющего ввод пользователя
                //editLimitsBinding.caloriesValueAdd.setText("CHANGED")
            }

            override fun afterTextChanged(sequence: Editable?) {
                with (editLimitsBinding) {
                    try {
                        editLimitsBinding.caloriesValueAdd.setText(calculateCalories(proteinsValueAddGramm.text.toString(), fatsValueAddGramm.text.toString(), carbsValueAddGramm.text.toString()).toString())
                    } catch (e: java.lang.NumberFormatException) {
                        Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                            .show()
                    }

                }

            }
        }
        with (editLimitsBinding) {
            proteinsValueAddGramm.setText(proteinsLimit.toString())
            fatsValueAddGramm.setText(fatsLimit.toString())
            carbsValueAddGramm.setText(carbsLimit.toString())
            caloriesValueAdd.setText(caloriesLimit.toString())

            proteinsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
            fatsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
            carbsValueAddGramm.addTextChangedListener(nutrientsValueWatcher)
        }
        editLimitsBinding.saveButton.setOnClickListener {
            val proteins = editLimitsBinding.proteinsValueAddGramm.text.toString()
            proteinsLimit = proteins.toFloat()
            val fats = editLimitsBinding.fatsValueAddGramm.text.toString()
            fatsLimit = fats.toFloat()
            val carbs = editLimitsBinding.carbsValueAddGramm.text.toString()
            carbsLimit = carbs.toFloat()
            val calories = editLimitsBinding.caloriesValueAdd.text.toString()
            caloriesLimit = calories.toFloat()
            viewModel.setLimit(NutrientsEntity(0, proteinsLimit, fatsLimit, carbsLimit, caloriesLimit))
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack()
        }


        // Inflate the layout for this fragment
        return editLimitsBinding.root
    }

    companion object {
        fun newInstance() = EditLimitsFragment()

    }

}
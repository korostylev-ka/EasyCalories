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
import ru.korostylev.easycalories.utils.AndroidUtils.calculateCalories
import ru.korostylev.easycalories.viewmodel.NutrientsViewModel
import kotlin.math.roundToInt

private const val PROTEINS = "proteinsLimit"
private const val FATS = "fatsLimit"
private const val CARBS = "carbsLimit"
private const val CALORIES = "caloriesLimit"
class EditLimitsFragment : Fragment() {
    var proteinsLimit = 0F
    var proteinsPercent = 0
    var fatsLimit = 0F
    var fatsPercent = 0
    var carbsLimit = 0F
    var carbsPercent = 0
    var caloriesLimit = 0F

    val viewModel: NutrientsViewModel by activityViewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("EditLimits", "onCreate")
        with(viewModel.liveDataLimits.value) {
            proteinsLimit = this?.proteins ?: 0F
            fatsLimit = this?.fats ?: 0F
            carbsLimit = this?.carbs ?: 0F
            caloriesLimit = this?.calories ?: 0F
            if (caloriesLimit != 0F) {
                proteinsPercent = (proteinsLimit * 4 * 100/ caloriesLimit).roundToInt()
                fatsPercent = (fatsLimit * 9 * 100 / caloriesLimit).roundToInt()
                carbsPercent = (carbsLimit * 4 * 100 / caloriesLimit).roundToInt()
            } else {
                proteinsPercent = 0
                fatsPercent = 0
                carbsPercent = 0
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val editLimitsBinding = FragmentEditLimitsBinding.inflate(layoutInflater)

        Log.d("EditLimits", "onCreateView")
        requireActivity().setTitle(R.string.editCaloriesLimitFragment)

        val nutrientsPercentValueWatcher = object : TextWatcher {
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

            }

            override fun afterTextChanged(sequence: Editable?) {
                with (editLimitsBinding) {
                    try {
                        proteinsValueAddGramm.setText("1")
                        proteinsValueAddPercent.setText("2")
                        /*val proteinsGramm = (proteinsValueAddGramm.text.toString())
                        val proteinsGrammFloat = proteinsGramm.toFloat()
                        val fatsGramm = (fatsValueAddGramm.text.toString())
                        val fatsGrammFloat = fatsGramm.toFloat()
                        val carbsGramm = (carbsValueAddGramm.text.toString())
                        val carbsGrammFloat = carbsGramm.toFloat()
                        val proteinsPercent = proteinsValueAddPercent.text.toString()
                        val proteinsPercentFloat = proteinsPercent.toFloat()
                        val fatsPercent = fatsValueAddPercent.text.toString()
                        val fatsPercentFloat = proteinsPercent.toFloat()
                        val carbsPercent = carbsValueAddPercent.text.toString()
                        val carbsPercentFloat = carbsPercent.toFloat()
                        val caloriesValue = caloriesValueAdd.text.toString()
                        val caloriesFloat = caloriesValue.toFloat()
                        val proteinsValueGramm = (proteinsPercentFloat * caloriesFloat / (100 * 4)).roundToInt()
                        val fatsValueGramm = (fatsPercentFloat * caloriesFloat / (100 * 4)).roundToInt()
                        val carbsValueGramm = (carbsPercentFloat * caloriesFloat / (100 * 4)).roundToInt()

                        //caloriesValueAdd.setText(caloriesValue.toString())
                        caloriesValueAdd.setText(caloriesValue)
                        proteinsValueAddPercent.setText(proteinsPercent.toString())
                        fatsValueAddPercent.setText(fatsPercent.toString())
                        carbsValueAddPercent.setText(carbsPercent.toString())
                        proteinsValueAddGramm.setText(proteinsValueGramm.toString())
                        fatsValueAddGramm.setText(fatsValueGramm.toString())
                        carbsValueAddGramm.setText(carbsValueGramm.toString())*/
                    } catch (e: java.lang.NumberFormatException) {
                        Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                            .show()
                    }

                }

            }
        }

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
                        var proteinsPercent = 0
                        var fatsPercent = 0
                        var carbsPercent = 0
                        val proteinsGramm = (proteinsValueAddGramm.text.toString())
                        val proteinsGrammFloat = proteinsGramm.toFloat()
                        val fatsGramm = (fatsValueAddGramm.text.toString())
                        val fatsGrammFloat = fatsGramm.toFloat()
                        val carbsGramm = (carbsValueAddGramm.text.toString())
                        val carbsGrammFloat = carbsGramm.toFloat()
                        val caloriesValue = calculateCalories(proteinsGrammFloat, fatsGrammFloat, carbsGrammFloat)
                        val caloriesFloat = caloriesValue.toFloat()
                        if (caloriesFloat != 0F) {
                            proteinsPercent = (proteinsGrammFloat * 4 / caloriesFloat * 100).roundToInt()
                            fatsPercent = (fatsGrammFloat * 9 / caloriesFloat * 100).roundToInt()
                            carbsPercent = (carbsGrammFloat * 4 / caloriesFloat * 100).roundToInt()
                        }
                        caloriesValueAdd.setText(caloriesValue.toString())
                        proteinsValueAddPercent.setText(proteinsPercent.toString())
                        fatsValueAddPercent.setText(fatsPercent.toString())
                        carbsValueAddPercent.setText(carbsPercent.toString())
                    } catch (e: java.lang.NumberFormatException) {
                        Toast.makeText(context, R.string.numberFormatException, Toast.LENGTH_LONG)
                            .show()
                    } catch (e: java.lang.IllegalArgumentException) {
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
            proteinsValueAddPercent.setText(proteinsPercent.toString())
            fatsValueAddPercent.setText(fatsPercent.toString())
            carbsValueAddPercent.setText(carbsPercent.toString())
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
package ru.korostylev.easycalories.ui

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.dao.ProfileDao
import ru.korostylev.easycalories.databinding.FragmentProfileBinding
import ru.korostylev.easycalories.db.ProfileDB
import ru.korostylev.easycalories.entity.ProfileEntity
import ru.korostylev.easycalories.viewmodel.ProfileViewModel
import kotlin.coroutines.coroutineContext
import kotlin.math.pow

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var currentProfile: ProfileEntity? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var nameV: String? = null
    private var genderV: String? = null
    private var ageV: Int? = null
    private var weightV: Float? = null
    private var heightV: Int? = null
    private var chestV: Float? = null
    private var waistV: Float? = null
    private var hipV: Float? = null
    private var neckV: Float? = null
    private var bmiV: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentProfile = profileViewModel.getProfile()
        with(currentProfile) {
            nameV = this?.name
            genderV = this?.gender ?: getString(R.string.male)
            ageV = this?.age
            weightV = this?.weight
            heightV = this?.height
            chestV = this?.chest
            waistV = this?.waist
            hipV = this?.hip
            neckV = this?.neck
            bmiV = this?.bmi
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        fun calcBMI() {
            if (heightV != null && weightV != null) {
                val bmi = weightV!! / ((heightV!!.toFloat() / 100F).pow(2.0F))
                bmiV = Math.round(bmi * 10F) / 10F
                profileBinding.bmiValue.text = bmiV.toString()
                when (bmiV ?: 0F) {
                    in 0.1F..15.99F -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_severe)
                    in 16F..18.49F -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_mild)
                    in 18.5F..24.99F -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_normal)
                    in 25F..29.99F -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_overweight)
                    in 30F..34.99F -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese1)
                    in 35F..39.99F -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese2)
                    in 40F..Float.MAX_VALUE -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese3)
                    else -> profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_no_gradient)
                }

            } else {
                bmiV = null
                profileBinding.bmiValue.text = ""
                profileBinding.bmiValue.setBackgroundResource(R.drawable.edit_text_no_gradient)
            }
        }
        val digitFieldWatcher = object : TextWatcher {
            var position = 0
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                var position = 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                position = start
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.startsWith('0')) {
                    s?.delete(position, position + 1)
                    Toast.makeText(context, R.string.startFrom0, Toast.LENGTH_LONG)
                        .show()
                }
                if ((text.startsWith('.')) || (text.startsWith(','))) {
                    s?.delete(position, position + 1)
                    Toast.makeText(context, R.string.startFrom_, Toast.LENGTH_LONG)
                        .show()
                }
            }

        }
        with(profileBinding) {
            nameValue.setText(nameV ?: "")
            ageValue.setText(ageV?.toString() ?: "")
            genderValue.setText(genderV)
            weightValue.setText(weightV?.toString() ?: "")
            heightValue.setText(heightV?.toString() ?: "")
            chestValue.setText(chestV?.toString() ?: "")
            waistValue.setText(waistV?.toString() ?: "")
            hipValue.setText(hipV?.toString() ?: "")
            neckValue.setText(neckV?.toString() ?: "")
            bmiValue.setText(bmiV?.toString() ?: "")
            when (bmiV ?: 0F) {
                in 0.1F..15.99F -> bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_severe)
                in 16F..18.49F -> bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_mild)
                in 18.5F..24.99F -> bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_normal)
                in 25F..29.99F -> bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_overweight)
                in 30F..34.99F -> bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese1)
                in 35F..39.99F -> bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese2)
                in 40F..Float.MAX_VALUE -> bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese3)
                else -> bmiValue.setBackgroundResource(R.drawable.edit_text_no_gradient)
            }
            weightValue.addTextChangedListener(digitFieldWatcher)
            ageValue.addTextChangedListener(digitFieldWatcher)
            heightValue.addTextChangedListener(digitFieldWatcher)
            chestValue.addTextChangedListener(digitFieldWatcher)
            waistValue.addTextChangedListener(digitFieldWatcher)
            hipValue.addTextChangedListener(digitFieldWatcher)
            neckValue.addTextChangedListener(digitFieldWatcher)
            buttonSave.setOnClickListener {
                nameV = nameValue.text.toString()
                ageV = convertIntEditTextValues(ageValue)
                genderV = genderValue.text.toString()
                weightV = convertFloatEditTextValues(weightValue)
                heightV = convertIntEditTextValues(heightValue)
                chestV = convertFloatEditTextValues(chestValue)
                waistV = convertFloatEditTextValues(waistValue)
                hipV = convertFloatEditTextValues(hipValue)
                neckV = convertFloatEditTextValues(neckValue)
                calcBMI()
//                bmiV = convertFloatEditTextValues(bmiValue as EditText)
                val newProfile = ProfileEntity(0, nameV, genderV, ageV, weightV, heightV, chestV, waistV, hipV, neckV, bmiV)
                profileViewModel.saveProfile(newProfile)

            }
            buttonReturn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
        profileBinding.genderValue.setOnClickListener {
            PopupMenu(context, it).apply {
                inflate(R.menu.gender_menu)
                setOnMenuItemClickListener {item ->
                    when (item.itemId) {
                        R.id.male -> {
                            profileBinding.genderValue.setText(R.string.male)
                            true
                        }
                        R.id.female -> {
                            profileBinding.genderValue.setText(R.string.female)
                            true
                        }
                        else -> false
                    }


                }
            }
                .show()
        }
        // Inflate the layout for this fragment
        return profileBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    fun convertFloatEditTextValues(editText: EditText): Float? {
        val value = editText.text.toString()
        if (value.isEmpty()) {
            return null
        } else {
            try {
                val newValue = Math.round(value.toFloat() * 10.0F) / 10.0F
                return newValue
            } catch (e: NumberFormatException) {
                Toast.makeText(context, R.string.checkTheFieldsAreCorrect, Toast.LENGTH_LONG)
                    .show()
                throw Exception(e)
            }
        }
    }

    fun convertIntEditTextValues(editText: EditText): Int? {
        val value = editText.text.toString()
        if (value.isEmpty()) {
            return null
        } else {
            try {
                return value.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, R.string.checkTheFieldsAreCorrect, Toast.LENGTH_LONG)
                    .show()
                throw Exception(e)
            }
        }
    }




}
package ru.korostylev.easycalories.ui

import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat
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


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding is null")
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var currentProfile: ProfileEntity? = null
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
    ): View {
        requireActivity().setTitle(R.string.profile)
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDigitInputTextWatcher()
        addClickListeners()
        bindViews()
        calcBMI()
    }

    @SuppressLint("ResourceAsColor")
    private fun calcBMI() {
        if (heightV != null && weightV != null) {
            val bmi = weightV!! / ((heightV!!.toFloat() / 100F).pow(2.0F))
            bmiV = Math.round(bmi * 10F) / 10F
            binding.bmiValue.text = bmiV.toString()
            binding.bmiStatus.visibility = View.VISIBLE
            when (bmiV ?: 0F) {
                in 0.1F..15.99F -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_severe)
                    binding.bmiValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_severe_thinness))
                    binding.bmiStatus.setText(R.string.BMISevereThinness)
                    binding.bmiStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_severe_thinness))
                }
                in 16F..18.49F -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_mild)
                    binding.bmiValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_mild_thinness))
                    binding.bmiStatus.setText(R.string.BMIMildThinness)
                    binding.bmiStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_mild_thinness))
                }
                in 18.5F..24.99F -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_normal)
                    binding.bmiValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_normal))
                    binding.bmiStatus.setText(R.string.BMINormalRange)
                    binding.bmiStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_normal))
                }
                in 25F..29.99F -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_overweight)
                    binding.bmiValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_overweight))
                    binding.bmiStatus.setText(R.string.BMIOverweight)
                    binding.bmiStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_overweight))
                }
                in 30F..34.99F -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese1)
                    binding.bmiValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese1))
                    binding.bmiStatus.setText(R.string.BMIObeseClassI)
                    binding.bmiStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese1))
                }
                in 35F..39.99F -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese2)
                    binding.bmiValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese2))
                    binding.bmiStatus.setText(R.string.BMIObeseClassII)
                    binding.bmiStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese2))
                }
                in 40F..Float.MAX_VALUE -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_bmi_obese3)
                    binding.bmiValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese3))
                    binding.bmiStatus.setText(R.string.BMIObeseClassIII)
                    binding.bmiStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.bmi_obese3))
                }
                else -> {
                    binding.bmiValue.setBackgroundResource(R.drawable.edit_text_no_gradient)
                    binding.bmiStatus.visibility = View.GONE
                }
            }

        } else {
            bmiV = null
            binding.bmiValue.text = ""
            binding.bmiValue.setBackgroundResource(R.drawable.edit_text_no_gradient)
            binding.bmiStatus.visibility = View.GONE
        }
    }

    private fun addDigitInputTextWatcher() {
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
        binding.weightValue.addTextChangedListener(digitFieldWatcher)
        binding.ageValue.addTextChangedListener(digitFieldWatcher)
        binding.heightValue.addTextChangedListener(digitFieldWatcher)
        binding.chestValue.addTextChangedListener(digitFieldWatcher)
        binding.waistValue.addTextChangedListener(digitFieldWatcher)
        binding.hipValue.addTextChangedListener(digitFieldWatcher)
        binding.neckValue.addTextChangedListener(digitFieldWatcher)
    }

    private fun addClickListeners() {
        binding.buttonSave.setOnClickListener {
            nameV = binding.nameValue.text.toString()
            ageV = convertIntEditTextValues(binding.ageValue)
            genderV = binding.genderValue.text.toString()
            weightV = convertFloatEditTextValues(binding.weightValue)
            heightV = convertIntEditTextValues(binding.heightValue)
            chestV = convertFloatEditTextValues(binding.chestValue)
            waistV = convertFloatEditTextValues(binding.waistValue)
            hipV = convertFloatEditTextValues(binding.hipValue)
            neckV = convertFloatEditTextValues(binding.neckValue)
            calcBMI()
//                bmiV = convertFloatEditTextValues(bmiValue as EditText)
            val newProfile = ProfileEntity(0, nameV, genderV, ageV, weightV, heightV, chestV, waistV, hipV, neckV, bmiV)
            profileViewModel.saveProfile(newProfile)

        }
        binding.buttonReturn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.genderValue.setOnClickListener {
            PopupMenu(context, it).apply {
                inflate(R.menu.gender_menu)
                setOnMenuItemClickListener {item ->
                    when (item.itemId) {
                        R.id.male -> {
                            binding.genderValue.setText(R.string.male)
                            true
                        }
                        R.id.female -> {
                            binding.genderValue.setText(R.string.female)
                            true
                        }
                        else -> false
                    }


                }
            }
                .show()
        }
    }

    private fun bindViews() {
        with(binding) {
            nameValue.setText(nameV ?: EMPTY_STRING_VALUE)
            ageValue.setText(ageV?.toString() ?: EMPTY_STRING_VALUE)
            genderValue.setText(genderV)
            weightValue.setText(weightV?.toString() ?: EMPTY_STRING_VALUE)
            heightValue.setText(heightV?.toString() ?: EMPTY_STRING_VALUE)
            chestValue.setText(chestV?.toString() ?: EMPTY_STRING_VALUE)
            waistValue.setText(waistV?.toString() ?: EMPTY_STRING_VALUE)
            hipValue.setText(hipV?.toString() ?: EMPTY_STRING_VALUE)
            neckValue.setText(neckV?.toString() ?: EMPTY_STRING_VALUE)
            bmiValue.setText(bmiV?.toString() ?: EMPTY_STRING_VALUE)
        }
    }

    companion object {
        private const val EMPTY_STRING_VALUE = ""
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
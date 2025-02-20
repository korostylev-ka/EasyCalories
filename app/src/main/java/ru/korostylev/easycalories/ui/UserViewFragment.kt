package ru.korostylev.easycalories.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.korostylev.easycalories.databinding.FragmentUserViewBinding

private const val DATE_ID = "dateId"

class UserViewFragment : Fragment() {
    private var date: Long = 0
    private var weight = date.toFloat()
    private var _binding: FragmentUserViewBinding? = null
    private val binding: FragmentUserViewBinding
        get() = _binding ?: throw RuntimeException("FragmentUserViewBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = requireArguments().getLong(DATE_ID)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserViewBinding.inflate(layoutInflater)
        binding.weightValue.setText(weight.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addClickListeners() {
        with(binding) {
            changeButton.setOnClickListener {
                weightValue.isFocusableInTouchMode = true
                weightValue.requestFocus()
                changeButton.visibility = View.GONE
                saveButton.visibility = View.VISIBLE
            }
            saveButton.setOnClickListener {
                weightValue.clearFocus()
                weightValue.isFocusable = false
                changeButton.visibility = View.VISIBLE
                saveButton.visibility = View.GONE

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(date: Long) = UserViewFragment().apply {
            arguments = Bundle().apply {
                putLong(DATE_ID, date)
            }
        }
    }


}
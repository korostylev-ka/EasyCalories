package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentUserViewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val DATE_ID = "dateId"

class UserViewFragment : Fragment() {
    var date: Long = 0
    var weight = date.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = arguments!!.getLong(DATE_ID)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userViewBinding = FragmentUserViewBinding.inflate(layoutInflater)
        userViewBinding.weightValue.setText(weight.toString())


        userViewBinding.changeButton.setOnClickListener {
            userViewBinding.weightValue.isFocusableInTouchMode = true
            userViewBinding.weightValue.requestFocus()
            userViewBinding.changeButton.visibility = View.GONE
            userViewBinding.saveButton.visibility = View.VISIBLE
        }
        userViewBinding.saveButton.setOnClickListener {
            userViewBinding.weightValue.clearFocus()
            userViewBinding.weightValue.isFocusable = false
            userViewBinding.changeButton.visibility = View.VISIBLE
            userViewBinding.saveButton.visibility = View.GONE

        }
        return userViewBinding.root
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
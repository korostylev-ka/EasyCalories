package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
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
        val profileBinding = FragmentProfileBinding.inflate(layoutInflater)
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
}
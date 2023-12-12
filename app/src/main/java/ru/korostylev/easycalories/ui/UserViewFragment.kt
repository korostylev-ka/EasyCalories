package ru.korostylev.easycalories.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.databinding.FragmentUserViewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userViewBinding = FragmentUserViewBinding.inflate(layoutInflater)
//        userViewBinding.menu.setOnClickListener {
//            PopupMenu(context, it).apply {
//                inflate(R.menu.options_menu)
//                setOnMenuItemClickListener {item ->
//                    when (item.itemId) {
//                        R.id.editLimits -> {
//                            requireParentFragment().parentFragmentManager.beginTransaction()
//                                .replace(R.id.fragmentContainer, EditLimitsFragment.newInstance())
//                                .addToBackStack(null)
//                                .commit()
//                            true
//                        }
//                        else -> false
//                    }
//
//
//                }
//            }
//                .show()
//        }
        return userViewBinding.root
    }


}
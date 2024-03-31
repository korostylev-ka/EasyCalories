package ru.korostylev.easycalories.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import ru.korostylev.easycalories.R


class AboutFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = androidx.appcompat.app.AlertDialog.Builder(it)
            val message = (getString(R.string.nameApp) + "\n" + getString(R.string.developer) + "\n" + getString(R.string.email))
            builder.setTitle(R.string.about)
                .setMessage(message)
//                .setIcon(R.drawable.hungrycat)
                .setPositiveButton(R.string.back) { dialog, id ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException(getString(R.string.nullActivity))


}

}
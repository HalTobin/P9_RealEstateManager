package com.openclassrooms.realestatemanager.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.openclassrooms.realestatemanager.R

class SearchEstateDialogFragment: AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_search_estate, null)
        builder.setView(view)

        setUpListener()

        return builder.create()
    }

    private fun setUpListener() {

    }

}
package com.example.nationapp.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.example.nationapp.R

class LoadingDialog(context: Context) {
    private val dialog: Dialog = Dialog(context).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
    }

    fun show(message: String? = null) {
        message?.let {
            dialog.findViewById<TextView>(R.id.loading_text).text = it
        }
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
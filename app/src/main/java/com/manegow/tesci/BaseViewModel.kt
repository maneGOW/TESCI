package com.manegow.tesci

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

open class BaseViewModel(application: Application) :
    AndroidViewModel(application) {

    fun generateWarningAlert(title: String, message: String, buttonText: String, context: Context) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setTitle(title)
        alertBuilder.setMessage(message)
        alertBuilder.setPositiveButton(buttonText) { _, _ -> }
        val alertDialog: AlertDialog = alertBuilder.create()
        alertDialog.show()
    }
}
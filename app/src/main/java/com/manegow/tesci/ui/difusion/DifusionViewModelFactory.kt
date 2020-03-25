package com.manegow.tesci.ui.difusion

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manegow.tesci.db.TesciDao
import com.manegow.tesci.ui.difusiondetail.DifusionDetailViewModel

class DifusionViewModelFactory(
    private val dataSource: TesciDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DifusionViewModel::class.java)) {
            return DifusionViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
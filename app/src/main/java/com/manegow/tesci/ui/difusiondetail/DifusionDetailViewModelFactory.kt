package com.manegow.tesci.ui.difusiondetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manegow.tesci.db.TesciDao

class DifusionDetailViewModelFactory(
    private val dataSource: TesciDao,
    private val noteId: Int,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DifusionDetailViewModel::class.java)) {
            return DifusionDetailViewModel(dataSource, noteId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
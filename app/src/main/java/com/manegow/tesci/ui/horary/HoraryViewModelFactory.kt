package com.manegow.tesci.ui.horary

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manegow.tesci.db.TesciDao
import com.manegow.tesci.ui.ratings.RatingsViewModel

class HoraryViewModelFactory(
    private val dataSource: TesciDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HoraryViewModel::class.java)) {
            return HoraryViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
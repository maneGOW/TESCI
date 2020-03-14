package com.manegow.tesci.ui.ratings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manegow.tesci.db.TesciDao

class RatingsViewModelFactory(
    private val dataSource: TesciDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingsViewModel::class.java)) {
            return RatingsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
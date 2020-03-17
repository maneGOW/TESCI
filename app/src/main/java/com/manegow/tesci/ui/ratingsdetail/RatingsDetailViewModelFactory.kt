package com.manegow.tesci.ui.ratingsdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manegow.tesci.db.TesciDao


class RatingsViewModelFactory(
    private val dataSource: TesciDao,
    private val semesterNumber: Long,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingsDetailViewModel::class.java)) {
            return RatingsDetailViewModel(dataSource, semesterNumber, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
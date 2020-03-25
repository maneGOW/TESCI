package com.manegow.tesci.ui.horarydetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manegow.tesci.db.TesciDao
import com.manegow.tesci.ui.horary.HoraryViewModel

class HoraryDetailViewModelFactory(
    private val dataSource: TesciDao,
    private val day:String,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HoraryDetailViewModel::class.java)) {
            return HoraryDetailViewModel(dataSource, day, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
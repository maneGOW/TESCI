package com.manegow.tesci.ui.horary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manegow.tesci.db.Days
import com.manegow.tesci.db.TesciDao
import kotlinx.coroutines.*

class HoraryViewModel(private val database: TesciDao, application: Application) :
    AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var day = MutableLiveData<Days?>()
    val days = database.getAllDays()

    private val _navigateToHoraryDetail = MutableLiveData<String>()
    val navigateToHoraryDetail: LiveData<String>
        get() = _navigateToHoraryDetail

    fun onDayClicked(day: String) {
        println("CLICKED")
        _navigateToHoraryDetail.value = day
    }

    fun onDayNavigated() {
        _navigateToHoraryDetail.value = null
    }

    init {
        initializeDays()
    }

    private fun initializeDays() {
        uiScope.launch {
            day.value = getDaysFromDatabase()
            if (day.value == null) {
                val daydata0 = Days(1, "Lunes")
                val daydata1 = Days(2, "Martes")
                val daydata2 = Days(3, "Miércoles")
                val daydata3 = Days(4, "Jueves")
                val daydata4 = Days(5, "Viernes")
                val daydata5 = Days(6, "Sábado")
                insert(daydata0)
                insert(daydata1)
                insert(daydata2)
                insert(daydata3)
                insert(daydata4)
                insert(daydata5)
                day.value = getDaysFromDatabase()
            }
        }
    }

    private suspend fun getDaysFromDatabase(): Days? {
        return withContext(Dispatchers.IO) {
            val semester = database.getDays()
            semester
        }
    }

    private suspend fun insert(days: Days) {
        withContext(Dispatchers.IO) {
            database.insertDay(days)
            println("dia insertado ${days.dayName}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

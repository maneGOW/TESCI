package com.manegow.tesci.ui.ratings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manegow.tesci.db.Semester
import com.manegow.tesci.db.TesciDao
import kotlinx.coroutines.*

class RatingsViewModel(private val database: TesciDao, application: Application) :
    AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var semester = MutableLiveData<Semester?>()
    val semesters = database.getAllSemesters()

    private val _navigateToRatingsDetail = MutableLiveData<Int>()
    val navigateToRatingsDetail: LiveData<Int>
        get() = _navigateToRatingsDetail

    fun onSemesterClicked(id: Int) {
        println("CLICKED")
        _navigateToRatingsDetail.value = id
    }

    fun onMovementNavigated() {
        _navigateToRatingsDetail.value = null
    }

    init {
        initializeSemesters()
    }

    private fun initializeSemesters() {
        uiScope.launch {
            semester.value = getSemestersFromDatabase()
            if (semester.value == null) {
                val semesterdata0 = Semester(1, 1)
                val semesterdata = Semester(2, 2)
                val semesterdata1 = Semester(3, 3)
                val semesterdata2 = Semester(4, 4)
                val semesterdata3 = Semester(5, 5)
                val semesterdata4 = Semester(6, 6)
                val semesterdata5 = Semester(7, 7)
                val semesterdata6 = Semester(8, 8)
                insert(semesterdata0)
                insert(semesterdata)
                insert(semesterdata1)
                insert(semesterdata2)
                insert(semesterdata3)
                insert(semesterdata4)
                insert(semesterdata5)
                insert(semesterdata6)
                semester.value = getSemestersFromDatabase()
            }
        }
    }

    private suspend fun getSemestersFromDatabase(): Semester? {
        return withContext(Dispatchers.IO) {
            val semester = database.getSemesters()
            semester
        }
    }

    private suspend fun insert(movement: Semester) {
        withContext(Dispatchers.IO) {
            database.insertSemester(movement)
            println("movimiento insertado ${movement.semesterNumber}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

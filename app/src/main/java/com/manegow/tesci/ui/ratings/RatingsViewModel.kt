package com.manegow.tesci.ui.ratings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manegow.tesci.db.Semester
import com.manegow.tesci.db.TesciDao
import kotlinx.coroutines.*

class RatingsViewModel(private val database: TesciDao, application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var semester = MutableLiveData<Semester?>()
    val semesters = database.getAllSemesters()

    private val _navigateToRatingsDetail = MutableLiveData<String>()
    val navigateToRatingsDetail: LiveData<String>
        get() = _navigateToRatingsDetail

    fun onSemesterClicked(id: String){
        _navigateToRatingsDetail.value = id
    }

    fun onMovementNavigated(){
        _navigateToRatingsDetail.value = null
    }

    init {
        initializeSemesters()
    }

    private fun initializeSemesters(){
        uiScope.launch {
           /* var numberSemster = 1
            while (numberSemster <= 9) {
                val semester = Semester(1,numberSemster)
                insert(semester)
                numberSemster += 1
            }*/
            semester.value = getSemestersFromDatabase()
        }
    }

    private suspend fun getSemestersFromDatabase(): Semester?{
        return withContext(Dispatchers.IO){
            val semester = database.getSemesters()
            semester
        }
    }

    private suspend fun insert(movement: Semester){
        withContext(Dispatchers.IO){
            database.insertSemester(movement)
            println("movimiento insertado ${movement.semesterNumber}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

/*


    private fun initializeMovements(){
        uiScope.launch {
            //val movementTest = Movements("66677788891234567890",System.currentTimeMillis().toString(),"1500.00","Prueba de pago 4s", "1","1", "1002","Pago de lentes")
            //insert(movementTest)
            movement.value = getMovementsFromDatabase()
            if(movement.value == null) _countMovements.value = "0"
            else _countMovements.value = movements.value!!.size.toString()
        }
    }

    private suspend fun getMovementsFromDatabase(): Movements?{
        return withContext(Dispatchers.IO){
            val movement = database.getMovementsByType(COBROS)
            movement
        }
    }

    private suspend fun update(movement: Movements){
        withContext(Dispatchers.IO){
            database.updateMovement(movement)
        }
    }

    private suspend fun insert(movement: Movements){
        withContext(Dispatchers.IO){
            database.insertMovement(movement)
            println("movimiento insertado ${movement.transactionFolio}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
 */
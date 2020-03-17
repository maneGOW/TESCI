package com.manegow.tesci.ui.ratingsdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.manegow.tesci.db.Rating
import com.manegow.tesci.db.TesciDao
import kotlinx.coroutines.*

class RatingsDetailViewModel(private val database: TesciDao, private val semesterNumber: Long, application: Application
) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var rating = MutableLiveData<Rating?>()
    val ratings = database.getRatingFromGrade(semesterNumber)

    //private val _navigateToRatingsDetail = MutableLiveData<Int>()
   // val navigateToRatingsDetail: LiveData<Int>
      //  get() = _navigateToRatingsDetail

    fun onRatingClicked(id: Int) {
        println("CLICKED")
     //   _navigateToRatingsDetail.value = id
    }

    /*fun onMovementNavigated() {
        _navigateToRatingsDetail.value = null
    }*/

    init {
        initializeSemesters()
    }

    private fun initializeSemesters() {
        uiScope.launch {
            rating.value = gerRatingsFromDatabaes(semesterNumber)
            if(rating.value == null){
                val semesterdata = Rating(1, 1,123107118,99.9F,"Cálculo Integral")
                val semesterdata1 = Rating(2, 2,123107118,88.8F,"Cálculo Diferencial")
                val semesterdata2 = Rating(3, 3,123107118,90.0F,"Programación Básica")
                val semesterdata3 = Rating(4, 4,123107118,75.0F,"Base de Datos 1")
                val semesterdata4 = Rating(5, 5,123107118,88.9F,"Programación web")
                val semesterdata5 = Rating(6, 6,123107118,79.7F,"Programación Móvil")
                val semesterdata6 = Rating(7, 7,123107118,88.6F,"Programación 2")
                val semesterdata7 = Rating(8, 8,123107118,97.6F,"Física")
                insert(semesterdata)
                insert(semesterdata1)
                insert(semesterdata2)
                insert(semesterdata3)
                insert(semesterdata4)
                insert(semesterdata5)
                insert(semesterdata6)
                insert(semesterdata7)
                rating.value = gerRatingsFromDatabaes(semesterNumber)
            }
        }
    }

    private suspend fun gerRatingsFromDatabaes(semester: Long): Rating? {
        return withContext(Dispatchers.IO) {
            val rating = database.getRating(semester)
            rating
        }
    }

    private suspend fun insert(rating: Rating) {
        withContext(Dispatchers.IO) {
            database.insertRating(rating)
            println("calificacion insertada ${rating.ratingName}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

package com.manegow.tesci.ui.horarydetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manegow.tesci.db.Days
import com.manegow.tesci.db.HoraryDetail
import com.manegow.tesci.db.TesciDao
import kotlinx.coroutines.*

class HoraryDetailViewModel(private val database: TesciDao, private val day:String, application: Application) :
    AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var detail = MutableLiveData<HoraryDetail?>()
    val details = database.getAllHoraryDetail(day)

   /* private val _navigateToHoraryDetail = MutableLiveData<Int>()
    val navigateToHoraryDetail: LiveData<Int>
        get() = _navigateToHoraryDetail*/

    fun onHoraryClicked(id: Int) {
        println("CLICKED")
    }

    init {
        initializeHoraryDetail(day)
    }

    private fun initializeHoraryDetail(day:String) {
        uiScope.launch {
            detail.value = getHoraryDetailFromDatabase(day)
            if (detail.value == null) {
                val horary0 = HoraryDetail(1, "7:00", "9:00", "Lunes","382-M","Cálculo Diferencial")
                val horary1 = HoraryDetail(2, "9:00", "11:00", "Lunes","382-M","Física")
                val horary2 = HoraryDetail(3, "7:00", "9:00", "Martes","382-M","Programación Básica")
                val horary3 = HoraryDetail(4, "9:00", "11:00", "Martes","382-M","Cálculo Integral")
                val horary4 = HoraryDetail(5, "7:00", "9:00", "Miércoles","382-M","Química")
                val horary5 = HoraryDetail(6, "9:00", "11:00", "Miércoles","382-M","Administración I")
                val horary6 = HoraryDetail(7, "7:00", "9:00", "Jueves","382-M","Base de Datos I")
                val horary7 = HoraryDetail(8, "9:00", "11:00", "Jueves","382-M","Programación Orientada a Eventos")
                val horary8 = HoraryDetail(9, "7:00", "9:00", "Viernes","382-M","Base de Datos II")
                val horary9 = HoraryDetail(10, "9:00", "11:00", "Viernes","382-M","Administración II")
                val horary10 = HoraryDetail(11, "11:00", "13:00", "Lunes","382-M","Matemáticas Discretas")
                val horary11 = HoraryDetail(12, "11:00", "13:00", "Martes","382-M","Ecuaciones Diferenciales")
                val horary12 = HoraryDetail(13, "11:00", "13:00", "Miércoles","382-M","Probabilidad y Estadística")
                val horary13 = HoraryDetail(14, "11:00", "13:00", "Jueves","382-M","Música")
                val horary14 = HoraryDetail(15, "11:00", "13:00", "Viernes","382-M","Educación Física")
                val horary15 = HoraryDetail(16, "7:00", "13:00", "Sábado","382-M","Inglés 3")
                insert(horary0)
                insert(horary1)
                insert(horary2)
                insert(horary3)
                insert(horary4)
                insert(horary5)
                insert(horary6)
                insert(horary7)
                insert(horary8)
                insert(horary9)
                insert(horary10)
                insert(horary11)
                insert(horary12)
                insert(horary13)
                insert(horary14)
                insert(horary15)
                detail.value = getHoraryDetailFromDatabase(day)
            }
        }
    }

    private suspend fun getHoraryDetailFromDatabase(horaryDayKey: String): HoraryDetail? {
        return withContext(Dispatchers.IO) {
            val horaryDetail = database.getHoraryDetial(horaryDayKey)
            horaryDetail
        }
    }

    private suspend fun insert(horaryDetail: HoraryDetail) {
        withContext(Dispatchers.IO) {
            database.insertHoraryDetail(horaryDetail)
            println("dia insertado ${horaryDetail.horaryNameSignature}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

package com.manegow.tesci.ui.difusiondetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manegow.tesci.db.DifusionNotes
import com.manegow.tesci.db.TesciDao
import kotlinx.coroutines.*

class DifusionDetailViewModel(private val database: TesciDao, private val noteId: Int, application: Application) :
    AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var note = MutableLiveData<DifusionNotes?>()
    val noteData : LiveData<DifusionNotes?>
    get() = note
    //val notes = database.getDifusionNoteDetail(noteId)

    init {
        initializeNoteData()
    }

    private fun initializeNoteData() {
        uiScope.launch {
            note.value = getNoteDataFromDatabase()
        }
    }

    private suspend fun getNoteDataFromDatabase(): DifusionNotes? {
        return withContext(Dispatchers.IO) {
            val notes = database.getDifusionNoteDetail(noteId)
            notes
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}


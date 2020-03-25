package com.manegow.tesci.ui.difusion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manegow.tesci.db.Days
import com.manegow.tesci.db.DifusionNotes
import com.manegow.tesci.db.TesciDao
import kotlinx.coroutines.*

class DifusionViewModel (private val database: TesciDao, application: Application) :
    AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var note = MutableLiveData<DifusionNotes?>()
    val notes = database.getAllDifusionNotesDetail()

    private val _navigateToDifusionNoteDetail = MutableLiveData<Long>()
    val navigateToDifusionNoteDetail: LiveData<Long>
        get() = _navigateToDifusionNoteDetail

    fun onNoteClicked(noteId: Long) {
        println("CLICKED")
        _navigateToDifusionNoteDetail.value = noteId
    }

    fun onNoteNavigated() {
        _navigateToDifusionNoteDetail.value = null
    }

    init {
        initializeNotes()
    }

    private fun initializeNotes() {
        uiScope.launch {
            note.value = getNotesFromDatabase()
            if (note.value == null) {
                val note0 = DifusionNotes(1, "Nuevas becas","BLABLABLABLAbLA","18/03/20202","18/04/20202","Manuel Pineda")
                val note1 = DifusionNotes(2, "Inscripciones","BLABLABLABLAbLA","18/03/20202","18/04/20202","Manuel Pineda")
                val note2 = DifusionNotes(3, "Taller de programación","BLABLABLABLAbLA","18/03/20202","18/04/20202","Manuel Pineda")
                val note3 = DifusionNotes(4, "Beca de Posgrado","BLABLABLABLAbLA","18/03/20202","18/04/20202","Manuel Pineda")
                insert(note0)
                insert(note1)
                insert(note2)
                insert(note3)
                note.value = getNotesFromDatabase()
            }
        }
    }

    private suspend fun getNotesFromDatabase(): DifusionNotes? {
        return withContext(Dispatchers.IO) {
            val notes = database.getDifuisonNotesDetails()
            notes
        }
    }

    private suspend fun insert(notes: DifusionNotes) {
        withContext(Dispatchers.IO) {
            database.inserDifusionNotes(notes)
            println("dia insertado ${notes.noteTitle}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

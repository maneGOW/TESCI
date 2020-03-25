package com.manegow.tesci.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "difusion_notes_table")
data class DifusionNotes(
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L,
    @ColumnInfo(name = "note_title")
    var noteTitle: String,
    @ColumnInfo(name = "note_body")
    var noteBody: String,
    @ColumnInfo(name = "note_date")
    var noteDate: String,
    @ColumnInfo(name = "note_expiration_date")
    var noteExpirationDate: String,
    @ColumnInfo(name = "note_autor")
    var noteAutor: String
)
package com.manegow.tesci.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "semesters_table")
data class Semester(

    @PrimaryKey(autoGenerate = true)
    var semesterId: Long = 0L,

    @ColumnInfo(name = "grade")
    var semesterNumber: Int
)
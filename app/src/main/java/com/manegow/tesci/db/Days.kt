package com.manegow.tesci.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days_table")
data class Days(
    @PrimaryKey(autoGenerate = true)
    var dayId: Long = 0L,
    @ColumnInfo(name = "day_name")
    var dayName: String
)
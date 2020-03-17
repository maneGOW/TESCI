package com.manegow.tesci.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rating_table")
data class Rating(
    @PrimaryKey(autoGenerate = true)
    var ratingId: Long = 0L,

    @ColumnInfo(name = "rating_school_grades")
    var ratingSchoolGrade: Long = 0L,

    @ColumnInfo(name = "rating_control_number")
    var ratingControlNumber: Long = 0L,

    @ColumnInfo(name = "rating_score")
    var ratingScore: Float = 0.0F,

    @ColumnInfo(name = "rating_name")
    var ratingName : String
)
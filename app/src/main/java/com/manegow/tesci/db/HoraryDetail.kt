package com.manegow.tesci.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horary_detail_table")
data class HoraryDetail(
    @PrimaryKey(autoGenerate = true)
    var horaryDetailId: Long = 0L,
    @ColumnInfo(name = "horary_time_start")
    var horaryTimeStart: String,
    @ColumnInfo(name = "horary_time_end")
    var horaryTimeEnd: String,
    @ColumnInfo(name = "horary_day")
    var horaryDay: String,
    @ColumnInfo(name = "horary_group")
    var horaryGroup: String,
    @ColumnInfo(name = "horary_name_signature")
    var horaryNameSignature: String
)
package com.manegow.tesci.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "user_username")
    var username: String,

    @ColumnInfo(name = "user_control_number")
    var userControlNumber: String,

    @ColumnInfo(name = "user_email")
    var email: String,

    @ColumnInfo(name = "user_password")
    var password: String,

    @ColumnInfo(name = "user_egistrationDate")
    var registrationDate: Long = System.currentTimeMillis()
)
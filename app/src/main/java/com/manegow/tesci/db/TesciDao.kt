package com.manegow.tesci.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TesciDao {

    //------------------------- users ----------------------
    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * from users_table WHERE userId = :userKey")
    fun getUser(userKey: Long): User

    @Query("DELETE FROM users_table")
    fun clearUsers()

    //------------------------- calificaciones ----------------------
    @Insert
    fun insertRating(rating: Rating)

    @Update
    fun updateRating(rating: Rating)

    @Query("SELECT * from rating_table WHERE ratingId = :ratingKey")
    fun getRating(ratingKey: Long): Rating

    @Query("DELETE FROM rating_table")
    fun clearRatings()

    //------------------------- semestres ----------------------
    @Insert
    fun insertSemester(semester: Semester)

    @Update
    fun updateSemester(semester: Semester)

    @Query("SELECT * FROM semesters_table WHERE semesterId = :semesterKey")
    fun getSemester(semesterKey: Long): Semester

    @Query("SELECT * FROM semesters_table ORDER BY semesterId DESC")
    fun getAllSemesters(): LiveData<List<Semester>>

    @Query("SELECT * FROM semesters_table ORDER BY semesterId DESC")
    fun getSemesters(): Semester?

    @Delete
    fun deleteSemester(semester: Semester)

    @Query("DELETE FROM semesters_table")
    fun deleteAllSemesters()

}

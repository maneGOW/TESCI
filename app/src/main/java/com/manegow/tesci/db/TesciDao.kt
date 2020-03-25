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

    @Query("SELECT * from rating_table WHERE rating_school_grades = :ratingSchoolGrade")
    fun getRatingFromGrade(ratingSchoolGrade: Long): LiveData<List<Rating>>

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

    @Query("SELECT * FROM semesters_table ORDER BY semesterId ASC")
    fun getAllSemesters(): LiveData<List<Semester>>

    @Query("SELECT * FROM semesters_table ORDER BY semesterId ASC")
    fun getSemesters(): Semester?

    @Delete
    fun deleteSemester(semester: Semester)

    @Query("DELETE FROM semesters_table")
    fun deleteAllSemesters()

    //------------------------ DAYS ----------------
    @Insert
    fun insertDay(day: Days)

    @Update
    fun updateDay(day: Days)

    @Query("SELECT * FROM days_table WHERE dayId = :dayKey")
    fun getDay(dayKey: Long): Days

    @Query("SELECT * FROM days_table ORDER BY dayId ASC")
    fun getAllDays(): LiveData<List<Days>>

    @Query("SELECT * FROM days_table ORDER BY dayId ASC")
    fun getDays(): Days?

    @Delete
    fun deleteDays(day: Days)

    @Query("DELETE FROM days_table")
    fun deleteAllDays()

    //------------------------ Details ----------------
    @Insert
    fun insertHoraryDetail(horaryDetail: HoraryDetail)

    @Update
    fun updateHoraryDetail(horaryDetail: HoraryDetail)

    @Query("SELECT * FROM horary_detail_table WHERE horary_day = :dayKey")
    fun getHoraryDetial(dayKey: String): HoraryDetail

    @Query("SELECT * FROM horary_detail_table WHERE horary_day =:dayKey ORDER BY horaryDetailId ASC")
    fun getAllHoraryDetail(dayKey:String): LiveData<List<HoraryDetail>>

    @Query("SELECT * FROM horary_detail_table ORDER BY horaryDetailId ASC")
    fun getHoraryDetails(): HoraryDetail?

    @Delete
    fun deleteHoraryDetails(horaryDetail: HoraryDetail)

    @Query("DELETE FROM horary_detail_table")
    fun deleteAllHoraryDetails()

    //------------------------ Difuison Notes ----------------
    @Insert
    fun inserDifusionNotes(difusionNotes: DifusionNotes)

    @Update
    fun updateHoraryDetail(difusionNotes: DifusionNotes)

    @Query("SELECT * FROM difusion_notes_table WHERE noteId = :noteId")
    fun getDifusionNoteDetail(noteId: Int): DifusionNotes

    @Query("SELECT * FROM difusion_notes_table ORDER BY noteId ASC")
    fun getAllDifusionNotesDetail(): LiveData<List<DifusionNotes>>

    @Query("SELECT * FROM difusion_notes_table ORDER BY noteId ASC")
    fun getDifuisonNotesDetails(): DifusionNotes?

    @Delete
    fun deleteHoraryDetails(difusionNotes: DifusionNotes)

    @Query("DELETE FROM difusion_notes_table")
    fun deleteAllDifusionNotes()
}

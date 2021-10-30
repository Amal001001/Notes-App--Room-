package com.example.notesapproom.Database

import androidx.room.*

@Dao
interface noteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Notes)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Notes>

    @Update
    suspend fun update(note: Notes)

    @Delete
    suspend fun delete(note: Notes)

    //    @Query("DELETE FROM notes")
//    fun deleteAll()
}
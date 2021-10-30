package com.example.notesapproom.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true) var id: Int,
   // @ColumnInfo(name = "note_column") val note: String
    val note: String
)
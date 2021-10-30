package com.example.notesapproom.Database

class NoteRepository (private val notedao: noteDao) {

    val getNotes: List<Notes> = notedao.getAllNotes()

    suspend fun insert(note: Notes){
        notedao.insert(note)
    }

    suspend fun update(note: Notes){
        notedao.update(note)
    }

    suspend fun delete(note: Notes){
        notedao.delete(note)
    }

}
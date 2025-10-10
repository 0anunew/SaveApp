package com.example.saveapp

import androidx.lifecycle.LiveData

class NoteRepository (private val noteDAO: NoteDAO){
    val allNotes: LiveData<MutableList<Note>> = noteDAO.getAllNotes()
    suspend fun insert(note: Note){
        noteDAO.insert(note)
    }
    suspend fun update(note: Note){
        noteDAO.update(note)
    }

    suspend fun delete(note: Note){
        noteDAO.delete(note)
    }

    suspend fun deleteAllNotes(){
        noteDAO.deleteAllNotes()
    }
    fun getNote(id: Int): Note {
        return noteDAO.getNote(id)
    }
}
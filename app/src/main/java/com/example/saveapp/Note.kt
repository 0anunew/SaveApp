package com.example.saveapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")

data class Note(

    val title: String,
    val content: String,
    val priority: Int
){
    @PrimaryKey(autoGenerate = true)
    var noteTableID = 0
}
package com.example.saveapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteDB : RoomDatabase() {
    abstract fun getNoteDao(): NoteDAO

    companion object{
        private var INSTANCE: NoteDB? = null
        fun getDatabaseInstance(context: Context): NoteDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, NoteDB::class.java, "note_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}
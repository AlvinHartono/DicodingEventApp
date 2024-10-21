package com.example.dicodingeventapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dicodingeventapp.data.local.entity.Event


@Database(entities = [Event::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao


    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null
        @JvmStatic
        fun getInstance(context: Context): EventDatabase {
            if (INSTANCE == null) {
                synchronized(EventDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        EventDatabase::class.java, "note_database")
                        .build()
                }
            }
            return INSTANCE as EventDatabase
        }
    }
}
package com.example.dicodingeventapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dicodingeventapp.data.local.entity.Event

@Dao
interface EventDao {

    //insert events
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvents(event: ArrayList<Event>)

    @Update
    fun update(note: Event)

    @Query("SELECT * FROM event WHERE isFavorite = 1")
    fun getFavoriteEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM event ORDER BY id ASC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND isFavorite = 1)")
    fun isEventFavorite(id: Int?): Boolean

    // delete upcoming events that is not favorite
    @Query("DELETE FROM event WHERE isFavorite = 0 AND isActive = 1")
    fun deleteAllUpcomingEvent()

    // delete finished events that is not favorite
    @Query("DELETE FROM event WHERE isFavorite = 0 AND isActive = 0")
    fun deleteAllFinishedEvent()
}
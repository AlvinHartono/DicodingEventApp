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
    suspend fun insertEvents(event: ArrayList<Event>)

    @Update
    suspend fun update(note: Event)

    @Query("SELECT * FROM event WHERE isFavorite = 1")
    fun getFavoriteEvents(): LiveData<List<Event>>

    //get finished events
    @Query("SELECT * FROM event WHERE isActive = '0'")
    fun getFinishedEvents(): LiveData<List<Event>>

    //get upcoming events
    @Query("SELECT * FROM event WHERE isActive = 1")
    fun getUpcomingEvents(): LiveData<List<Event>>

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND isFavorite = 1)")
    suspend fun isEventFavorite(id: Int?): Boolean

    // delete upcoming events that is not favorite
    @Query("DELETE FROM event WHERE isFavorite = 0 AND isActive = 1")
    suspend fun deleteAllUpcomingEvent()

    // delete finished events that is not favorite
    @Query("DELETE FROM event WHERE isFavorite = 0 AND isActive = 0")
    suspend fun deleteAllFinishedEvent()
}
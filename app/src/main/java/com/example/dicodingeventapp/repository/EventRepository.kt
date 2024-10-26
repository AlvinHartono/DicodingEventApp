package com.example.dicodingeventapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.dicodingeventapp.data.Result
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.data.local.room.EventDao
import com.example.dicodingeventapp.data.remote.retrofit.ApiService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
) {

    private val finishedResult = MediatorLiveData<Result<List<Event>>>()
    private val upcomingResult = MediatorLiveData<Result<List<Event>>>()
    private val searchResult = MediatorLiveData<Result<List<Event>>>()


    // fetch finished events data from the api
    suspend fun fetchFinishedEvents(): MutableLiveData<Result<List<Event>>> {
        Log.d("EventRepository", "Fetching finished events...")
        finishedResult.postValue(Result.Loading)

        try {
            Log.d("EventRepository", "Fetching finished events from API...")
            val response = apiService.fetchEvents(FINISHED_EVENTS)
            val events = response.listEvents
            Log.d(
                "EventRepository",
                "Finished events fetched from API: ${events?.size} and has data? ${!events.isNullOrEmpty()}"
            )

            if (!events.isNullOrEmpty()) {
                val eventList = ArrayList<Event>()
                events.forEach { event ->
                    Log.d("EventRepository", "Processing event: ${event?.name}")
                    val isFavorite = eventDao.isEventFavorite(event?.id)

                    val eventEntity = Event(
                        name = event?.name ?: "",
                        id = event?.id ?: 0,
                        summary = event?.summary ?: "",
                        mediaCover = event?.mediaCover ?: "",
                        registrants = event?.registrants ?: 0,
                        imageLogo = event?.imageLogo ?: "",
                        link = event?.link ?: "",
                        description = event?.description ?: "",
                        ownerName = event?.ownerName ?: "",
                        cityName = event?.cityName ?: "",
                        quota = event?.quota ?: 0,
                        beginTime = event?.beginTime ?: "",
                        endTime = event?.endTime ?: "",
                        category = event?.category ?: "",
                        isFavorite = isFavorite,
                        isActive = false,
                    )

                    Log.d("EventRepository", "Event added to list: ${eventEntity.id}")
                    eventList.add(eventEntity)
                    Log.d("EventRepository", "Finished Event list size: ${eventList.size}")
                }
                eventDao.insertEvents(eventList)

                finishedResult.postValue(Result.Success(eventList))
                Log.d(
                    "EventRepository",
                    "Finished events fetched successfully ${finishedResult.value}"
                )
            } else {
                finishedResult.postValue(Result.Error("No events found"))
                Log.d("EventRepository", "No events found")
            }
        } catch (e: Exception) {
            finishedResult.postValue(Result.Error("Failed to fetch finished events: ${e.message}"))
            Log.e("EventRepository", "Failed to fetch finished events: ${e.message}")
        }
        return finishedResult
    }

    // fetch upcoming events from the api
    suspend fun fetchUpcomingEvents(): MutableLiveData<Result<List<Event>>> {
        Log.d("EventRepository", "Fetching upcoming events...")
        upcomingResult.postValue(Result.Loading)
        try {
            Log.d("EventRepository", "Fetching upcoming events from API...")
            val response = apiService.fetchEvents(UPCOMING_EVENTS)
            val events = response.listEvents
            Log.d(
                "EventRepository",
                "Upcoming events fetched from API: ${events?.size} and has data? ${!events.isNullOrEmpty()}"
            )

            if (!events.isNullOrEmpty()) {
                val eventList = ArrayList<Event>()
                events.forEach { event ->
                    Log.d("EventRepository", "Processing event: ${event?.name}")
                    val isFavorite = eventDao.isEventFavorite(event?.id)

                    val eventEntity = Event(
                        name = event?.name ?: "",
                        id = event?.id ?: 0,
                        summary = event?.summary ?: "",
                        mediaCover = event?.mediaCover ?: "",
                        registrants = event?.registrants ?: 0,
                        imageLogo = event?.imageLogo ?: "",
                        link = event?.link ?: "",
                        description = event?.description ?: "",
                        ownerName = event?.ownerName ?: "",
                        cityName = event?.cityName ?: "",
                        quota = event?.quota ?: 0,
                        beginTime = event?.beginTime ?: "",
                        endTime = event?.endTime ?: "",
                        category = event?.category ?: "",
                        isFavorite = isFavorite,
                        isActive = true
                    )

                    Log.d("EventRepository", "Event added to list: ${eventEntity.id}")

                    eventList.add(eventEntity)
                    Log.d("EventRepository", "Event list size: ${eventList.size}")
                }

                // Insert into local DB and observe local data
                eventDao.insertEvents(eventList)
                Log.d("EventRepository", "Events inserted into local DB")


                upcomingResult.postValue(Result.Success(eventList))
                Log.d(
                    "EventRepository",
                    "Upcoming events fetched successfully ${finishedResult.value}"
                )

            } else {
                // Handle case when event list is empty or null
                upcomingResult.postValue(Result.Error("No events found"))
                Log.d("EventRepository", "No events found")

            }

        } catch (e: Exception) {
            // Post the error result in case of an exception
            upcomingResult.postValue(Result.Error("Failed to fetch upcoming events: ${e.message}"))
        }
        return upcomingResult
    }


    // fetch the search result from the api
    suspend fun fetchSearchResult(query: String): MutableLiveData<Result<List<Event>>> {
        Log.d("EventRepository", "Fetching search result for query: $query")
        searchResult.postValue(Result.Loading)
//        try {
            Log.d("EventRepository", "Fetching search result for query: $query")
            val response = apiService.findEvents(ALL_EVENTS, query)
            Log.d(
                "EventRepository",
                "Search result fetched from API: ${response.listEvents?.size} and has data? ${!response.listEvents.isNullOrEmpty()}"
            )
            val events = response.listEvents
            Log.d("EventRepository", "Search result fetched successfully")
            if (events!!.isNotEmpty()) {
                Log.d("EventRepository", "Search events is not empty")
                val eventList = ArrayList<Event>()
                events.forEach { event ->
                    val isFavorite = eventDao.isEventFavorite(event?.id)
                    val eventEntity = Event(
                        name = event?.name ?: "",
                        id = event?.id ?: 0,
                        summary = event?.summary ?: "",
                        mediaCover = event?.mediaCover ?: "",
                        registrants = event?.registrants ?: 0,
                        imageLogo = event?.imageLogo ?: "",
                        link = event?.link ?: "",
                        description = event?.description ?: "",
                        ownerName = event?.ownerName ?: "",
                        cityName = event?.cityName ?: "",
                        quota = event?.quota ?: 0,
                        beginTime = event?.beginTime ?: "",
                        endTime = event?.endTime ?: "",
                        category = event?.category ?: "",
                        isFavorite = isFavorite,
                        isActive = isEventActive(event?.beginTime),
                    )
                    Log.d("EventRepository", "Event added to list: ${eventEntity.id}")
                    eventList.add(eventEntity)
                    Log.d("EventRepository", "Event list size: ${eventList.size}")
                }
//                eventDao.insertEvents(eventList)
                searchResult.value = Result.Success(eventList)
                Log.d(
                    "EventRepository",
                    "Search result fetched successfullys ${searchResult.value}"
                )
            } else {
                searchResult.value = Result.Error("No events found")
                Log.d("EventRepository", "No events found")
            }
//        } catch (e: Exception) {
//            searchResult.postValue(Result.Error("Failed to fetch search result: ${e.message}"))
//            Log.e("EventRepository", "Failed to fetch search result: ${e.message}")
//        }
        return searchResult
    }


    fun getFavoriteEvents(): LiveData<List<Event>> {
        return eventDao.getFavoriteEvents()
    }


    suspend fun setFavoriteEvents(event: Event, eventState: Boolean) {
        event.isFavorite = eventState
        eventDao.update(event)
    }


    private fun isEventActive(beginTime: String?): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        return try {
            val eventDate = beginTime?.let { format.parse(it) }

            val currentDate = Date()

            eventDate?.after(currentDate) == true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val FINISHED_EVENTS = 0
        private const val UPCOMING_EVENTS = 1
        private const val ALL_EVENTS = -1

        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)

            }.also { instance = it }
    }

}
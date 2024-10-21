package com.example.dicodingeventapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.dicodingeventapp.data.Result
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.data.local.room.EventDao
import com.example.dicodingeventapp.data.remote.response.EventResponse
import com.example.dicodingeventapp.data.remote.retrofit.ApiService
import com.example.dicodingeventapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {


//    // get all event
//    fun getAllEvents() = mEventDao.getAllEvents()
//
//    // insert event
//    fun insert(event: Event) {
//        executorService.execute { mEventDao.insertEvent(event) }
//    }
//
//    // update event
//    fun update(event: Event) {
//        executorService.execute { mEventDao.update(event) }
//    }
//
//    // delete event
//    fun delete(event: Event) {
//        executorService.execute { mEventDao.delete(event) }
//    }
//
//    // get favorite event
//    fun getFavoriteEvents() = mEventDao.getFavoriteEvents()


    private val finishedResult = MediatorLiveData<Result<List<Event>>>()
    private val upcomingResult = MediatorLiveData<Result<List<Event>>>()
    private val searchResult = MediatorLiveData<Result<List<Event>>>()
    private val detailResult = MediatorLiveData<Result<Event>>()

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


    // fetch finished events data from the api
    fun fetchFinishedEvents(): MutableLiveData<Result<List<Event>>> {
        finishedResult.value = Result.Loading
        val client = apiService.getEvents(active = FINISHED_EVENTS)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    val eventList = ArrayList<Event>()
                    appExecutors.diskIO.execute {
                        events?.forEach { event ->
                            val isFavorite = eventDao.isEventFavorite(event?.id)
                            val eventEntity =
                                Event(
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
                                    isActive = false
                                )
                            eventList.add(eventEntity)
                        }
                        eventDao.deleteAllFinishedEvent()
                        eventDao.insertEvents(eventList)
                    }
                    Log.d("EventRepository", "Success, onResponse: $eventList")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                finishedResult.value = Result.Error(t.message.toString())
            }
        })

        val localData = eventDao.getAllEvents()
        finishedResult.addSource(localData) { newData: List<Event> ->
            val finishedEvents = newData.filter { event ->
                val eventBeginTime = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).parse(event.beginTime)
                eventBeginTime?.let {
                    it.time < System.currentTimeMillis() // Event has finished
                } ?: false
            }
            finishedResult.value = Result.Success(finishedEvents)
        }
        return finishedResult
    }

    // fetch upcoming events from the api
    fun fetchUpcomingEvents(): LiveData<Result<List<Event>>> {
        upcomingResult.value = Result.Loading
        val client = apiService.getEvents(active = UPCOMING_EVENTS)

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    val eventList = ArrayList<Event>()
                    appExecutors.diskIO.execute {
                        events?.forEach { event ->
                            val isFavorite = eventDao.isEventFavorite(event?.id)
                            val eventEntity =
                                Event(
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
                                    isActive = true,
                                )
                            eventList.add(eventEntity)
                        }
                        eventDao.deleteAllUpcomingEvent()
                        eventDao.insertEvents(eventList)
                    }
                    Log.d("EventRepository", "Success, onResponse: $eventList")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                upcomingResult.value = Result.Error(t.message.toString())
            }

        })
        val localData = eventDao.getAllEvents()
        upcomingResult.addSource(localData) { newData: List<Event> ->
            val upcomingEvents = newData.filter { event ->
                val eventBeginTime = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).parse(event.beginTime)
                eventBeginTime?.let {
                    it.time > System.currentTimeMillis() // Event has not started yet
                } ?: false
            }
            upcomingResult.value = Result.Success(upcomingEvents)
        }
        return upcomingResult
    }

    // fetch the search result from the api
    fun fetchSearchResult(query: String): LiveData<Result<List<Event>>> {
        finishedResult.value = Result.Loading
        val client = apiService.findEvents(active = ALL_EVENTS, query)

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    val eventList = ArrayList<Event>()
                    appExecutors.diskIO.execute {
                        events?.forEach { event ->
                            val isFavorite = eventDao.isEventFavorite(event?.id)
                            val eventEntity =
                                Event(
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
                                    isActive = true,
                                )
                            eventList.add(eventEntity)
                        }
                        eventDao.insertEvents(eventList)
                    }
                    searchResult.value = Result.Success(eventList)
                } else {
                    searchResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                searchResult.value = Result.Error(t.message.toString())
            }
        })

        return searchResult
    }

//    fun fetchDetailEvent(eventId: Int): LiveData<Result<Event>> {
//        detailResult.value = Result.Loading
//        val client = apiService.getDetailEvent(eventId)
//        client.enqueue(object : Callback<DetailEventResponse> {
//            override fun onResponse(
//                call: Call<DetailEventResponse>,
//                response: Response<DetailEventResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val event = response.body()?.event
//                    val isFavorite = eventDao.isEventFavorite(event?.id)
//                    val eventEntity = Event(
//                        name = event?.name ?: "",
//                        id = event?.id ?: 0,
//                        summary = event?.summary ?: "",
//                        mediaCover = event?.mediaCover ?: "",
//                        registrants = event?.registrants ?: 0,
//                        imageLogo = event?.imageLogo ?: "",
//                        link = event?.link ?: "",
//                        description = event?.description ?: "",
//                        ownerName = event?.ownerName ?: "",
//                        cityName = event?.cityName ?: "",
//                        quota = event?.quota ?: 0,
//                        beginTime = event?.beginTime ?: "",
//                        endTime = event?.endTime ?: "",
//                        category = event?.category ?: "",
//                        isFavorite = isFavorite,
//                        isActive = (event?.beginTime?.let {
//                            dateFormat.parse(it)?.time ?: 0L
//                        } ?: 0L) < System.currentTimeMillis(),
//                    )
//                    detailResult.value = Result.Success(eventEntity)
//
//                } else {
//                    detailResult.value = Result.Error(response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
//                detailResult.value = Result.Error(t.message.toString())
//            }
//
//        })
//        return detailResult
//    }

    fun getFavoriteEvents(): LiveData<List<Event>> {
        return eventDao.getFavoriteEvents()
    }

    fun setFavoriteEvents(event: Event, eventState: Boolean) {
        appExecutors.diskIO.execute {
            event.isFavorite = eventState
            eventDao.update(event)
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
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)

            }.also { instance = it }
    }

}
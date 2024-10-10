package com.example.dicodingeventapp.data.retrofit

import com.example.dicodingeventapp.data.response.DetailEventResponse
import com.example.dicodingeventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // get events
    @GET("events")
    fun getEvents(@Query("param") param: String): Call<EventResponse>

    // get detail event
    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: Int): Call<DetailEventResponse>

    // get search event
    @GET("events")
    fun findEvents(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String = "",
        @Query("limit") limit: Int = 40
    ): Call<EventResponse>



//
//    @GET("events={id}")
//    fun getEvent(@Path("id") id: String): Call<EventResponse>
}
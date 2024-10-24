package com.example.dicodingeventapp.data.remote.retrofit

import com.example.dicodingeventapp.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // get events
    @GET("events")
    suspend fun fetchEvents(@Query("active") active: Int = -1): EventResponse

    // get search event
    @GET("events")
    suspend fun findEvents(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String = "",
        @Query("limit") limit: Int = 40
    ): EventResponse

}
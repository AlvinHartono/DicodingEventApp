package com.example.dicodingeventapp.data.retrofit

import com.example.dicodingeventapp.data.response.DetailEventResponse
import com.example.dicodingeventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(@Query("param") param: String): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<DetailEventResponse>

//
//    @GET("events={id}")
//    fun getEvent(@Path("id") id: String): Call<EventResponse>
}
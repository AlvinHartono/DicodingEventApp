package com.example.dicodingeventapp.data.di

import android.content.Context
import com.example.dicodingeventapp.data.local.room.EventDatabase
import com.example.dicodingeventapp.data.remote.retrofit.ApiConfig
import com.example.dicodingeventapp.repository.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}
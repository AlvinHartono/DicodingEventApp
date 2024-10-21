package com.example.dicodingeventapp.data.di

import android.content.Context
import com.example.dicodingeventapp.data.local.room.EventDatabase
import com.example.dicodingeventapp.data.remote.retrofit.ApiConfig
import com.example.dicodingeventapp.repository.EventRepository
import com.example.dicodingeventapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}
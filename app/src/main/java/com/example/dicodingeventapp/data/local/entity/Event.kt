package com.example.dicodingeventapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Event(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "summary")
    val summary: String = "",

    @ColumnInfo(name = "mediaCover")
    var mediaCover: String = "",

    @ColumnInfo(name = "registrants")
    val registrants: Int = 0,

    @ColumnInfo(name = "imageLogo")
    val imageLogo: String = "",

    @ColumnInfo(name = "link")
    val link: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "ownerName")
    val ownerName: String = "",

    @ColumnInfo(name = "cityName")
    val cityName: String = "",

    @ColumnInfo(name = "quota")
    val quota: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "beginTime")
    val beginTime: String = "",

    @ColumnInfo(name = "endTime")
    val endTime: String = "",

    @ColumnInfo(name = "category")
    val category: String = "",

    //check if the event is active
    @ColumnInfo(name = "isActive")
    var isActive: Boolean = false,

    // Add favorite property with default value false
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
) : Parcelable
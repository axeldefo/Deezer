package com.example.deezer.service.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class Track(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "duration")
    val duration: Int,
    @ColumnInfo(name = "preview")
    val preview: String,
    @ColumnInfo(name = "title")
    val title: String,
    val disk_number: Int,
    val explicit_content_cover: Int,
    val explicit_content_lyrics: Int,
    val explicit_lyrics: Boolean,
    val isrc: String,
    val link: String,
    val md5_image: String,
    val rank: Int,
    val readable: Boolean,
    val title_short: String,
    val title_version: String,
    val track_position: Int,
    val type: String
)
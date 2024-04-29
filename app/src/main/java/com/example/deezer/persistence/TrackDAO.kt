package com.example.deezer.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.deezer.service.data.Track

@Dao
interface TrackDAO {

    @Query("SELECT * FROM track_table ORDER BY title ASC")
    fun getAllTracks(): List<Track>

    @Query("SELECT * FROM track_table WHERE title = :title")
    fun getTrackByTitle(title: String): Track

    @Query("SELECT * FROM track_table WHERE id = :id")
    fun getTrackById(id: Long): Track

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(track: Track)

    @Update
    fun update(track: Track)

    @Delete
    fun delete(track: Track)
}
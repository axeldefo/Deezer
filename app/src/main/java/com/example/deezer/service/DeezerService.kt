package com.example.deezer.service

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder


class DeezerService() {


    companion object {
        const val TAG = "DeezerService"
        val client = OkHttpClient()
    }
        fun searchArtist(artist: String, callback : CallSearchArtist){

            Log.d(TAG, "searchArtist: $artist")

            val vurl = "https://api.deezer.com/search/artist?q=${URLEncoder.encode(artist, "UTF-8")}"
            Log.d(TAG, "URL: $vurl")

            val request: Request = Request.Builder()
                .url(vurl)
                .build()

            client.newCall(request).enqueue(callback)
        }
        fun searchAlbum(artist: Int, callback: com.example.deezer.service.CallSearchAlbum){

            Log.d(TAG, "searchAlbum: ")

            val vurl = "https://api.deezer.com/artist/${artist}/albums"
            Log.d(TAG, "URL: $vurl")

            val request: Request = Request.Builder()
                .url(vurl)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(callback)
            Log.d(TAG, "searchAlbum: $artist")

        }

    fun searchTitle(albumid: Long, callback: com.example.deezer.service.CallSearchTitle){

        Log.d(TAG, "searchTitle: ")

        val vurl = "https://api.deezer.com/album/$albumid/tracks"
        Log.d(TAG, "URL: $vurl")

        val request: Request = Request.Builder()
            .url(vurl)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(callback)
        Log.d(TAG, "searchtitles of the album: $albumid")

    }






}

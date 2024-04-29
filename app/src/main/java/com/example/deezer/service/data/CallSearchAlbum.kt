package com.example.deezer.service.data

import android.util.Log
import com.example.deezer.service.DeezerService
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

abstract class CallSearchAlbum : Callback {

        companion object {
            const val TAG = "CallSearchAlbum"
        }

        abstract fun fireOnResponseOK(dataSearchAlbum: DataSearchAlbum)

        override fun onFailure(call: Call, e: IOException) {
            Log.e(DeezerService.TAG, ">>>onFailure: ", e)
        }

        // Parse response using gson deserializer
        override fun onResponse(call: Call, response: Response) {
            try {
                Log.d(TAG, ">>>onResponse: ")

                val gson = Gson()

                val responseData = response.body!!.string()

                // Process the data on the worker thread
                val dataSearchAlbum = gson.fromJson(responseData, DataSearchAlbum::class.java)
                // Access deserialized user object here
                Log.d(DeezerService.TAG, "onResponse: ${dataSearchAlbum.total} artists")

                fireOnResponseOK(dataSearchAlbum)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
}
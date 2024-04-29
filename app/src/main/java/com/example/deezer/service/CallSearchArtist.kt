package com.example.deezer.service

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.deezer.service.data.DataSearchArtist
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

abstract class CallSearchArtist : Callback {

    companion object {
        const val TAG = "CallSearchArtist"
    }

    abstract fun fireOnResponseOK(dataSearchArtist: DataSearchArtist)

    override fun onFailure(call: Call, e: IOException) {
        Log.e(TAG, ">>>onFailure: ", e)
    }

    // Parse response using gson deserializer
    override fun onResponse(call: Call, response: Response) {
        Log.d(TAG, ">>>onResponse: ")

        val gson = Gson()

        val responseData = response.body!!.string()

        // Process the data on the worker thread
        val dataSearchArtist = gson.fromJson(responseData, DataSearchArtist::class.java)
        // Access deserialized user object here
        Log.d(DeezerService.TAG, "onResponse: ${dataSearchArtist.total} artists")

        fireOnResponseOK(dataSearchArtist)
    }
}
package com.example.deezer.service

import android.util.Log
import com.example.deezer.service.data.DataSearchArtist
import com.example.deezer.service.data.DataSearchTitle
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

abstract class CallSearchTitle : Callback {

    companion object {
        const val TAG = "CallSearchTitle"
    }

    abstract fun fireOnResponseOK(dataSearchTitle: DataSearchTitle)

    override fun onFailure(call: Call, e: IOException) {
        Log.e(TAG, ">>>onFailure: ", e)
    }

    // Parse response using gson deserializer
    override fun onResponse(call: Call, response: Response) {
        Log.d(TAG, ">>>onResponse: ")

        val gson = Gson()

        val responseData = response.body!!.string()

        // Process the data on the worker thread
        val dataSearchTitle = gson.fromJson(responseData, DataSearchTitle::class.java)
        // Access deserialized user object here
        Log.d(DeezerService.TAG, "onResponse: ${dataSearchTitle.total} titres")

        fireOnResponseOK(dataSearchTitle)
    }
}
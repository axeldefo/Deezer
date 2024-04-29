package com.example.deezer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.example.deezer.persistence.TrackDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppApplication : Application() {
    companion object {
        val TAG: String = "Application"
        lateinit var context: Context private set
        val applicationScope = CoroutineScope(SupervisorJob())

        val database by lazy {
            TrackDataBase.getDatabase(context, applicationScope)
        }
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        applicationScope.launch {
            Log.d(TAG, "Size towns: " + database.trackDAO().getAllTracks().size)
        }
    }
}
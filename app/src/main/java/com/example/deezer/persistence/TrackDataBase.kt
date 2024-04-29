package com.example.deezer.persistence

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.deezer.service.data.Track
import com.example.deezer.ui.dashboard.DashboardFragment
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Track::class], version = 1, exportSchema = false)
abstract class TrackDataBase: RoomDatabase() {

    abstract fun trackDAO(): TrackDAO

    companion object {

        val TAG: String = "TrackDataBase"

        @Volatile
        private var INSTANCE: TrackDataBase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): TrackDataBase {

            return INSTANCE ?: synchronized(this) {
                Log.d(TAG, "getDatabase: ")
                Room.databaseBuilder(context, TrackDataBase::class.java, "track_database")
                    .addCallback(TrackDatabaseCallback(scope))
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    private class TrackDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        //override oncreate and onopen
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d(TAG, "onCreate: ")
            /*INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.trackDAO())
                }
            }*/
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d(TAG, "onOpen: ")
        }
    }


}

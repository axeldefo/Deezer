package com.example.deezer.ui.dashboard

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deezer.AppApplication
import com.example.deezer.R
import com.example.deezer.databinding.FavorisRowBinding
import com.example.deezer.persistence.TrackDataBase
import com.example.deezer.service.data.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FavRecycleViewAdapter(private var favs: List<Track>, private val recyclerView: RecyclerView, 
                            mediaP: MediaPlayer = MediaPlayer())
    : RecyclerView.Adapter<FavRecycleViewAdapter.FavViewHolder>() {
    companion object {
        private const val TAG = "FavRecycleViewAdapter"
    }

    val mediaPlayer = mediaP

    init {
        Log.d(TAG, "FavRecycleViewAdapter : ${favs.size}")
    }


    inner class FavViewHolder(val binding: FavorisRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val binding = FavorisRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(binding)
    }

    override fun getItemCount(): Int = favs.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {


        Log.d(TAG, "onBindViewHolder position=${position}")

        val fav: Track = favs[position]
        Log.d(TAG, " Fav ${fav.title}")

        val listenerView = ListenerView(this, recyclerView, mediaPlayer)

        holder.binding.num.text = (position + 1).toString()
        holder.binding.titre.text = fav.title

        val min = fav.duration / 60
        val sec = fav.duration % 60
        holder.binding.duree.text = "$min:$sec"

        holder.binding.audio.setOnClickListener {
            Log.d(TAG, "onClick: ${fav.id}")


            mediaPlayer.apply {
                if (mediaPlayer.isPlaying && listenerView.currentPlaying == holder) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    holder.binding.audio.setImageResource(R.drawable.play)
                    listenerView.onTitleStop()
                } else if (!mediaPlayer.isPlaying) {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(fav.preview)
                    prepare() // might take long! (for buffering, etc)
                    start()
                    Log.d(TAG, "playing: ${fav.preview}")

                    listenerView.onTitleSelected(holder, fav)

                } else if (mediaPlayer.isPlaying && listenerView.currentPlaying != holder) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(fav.preview)
                    prepare() // might take long! (for buffering, etc)
                    start()
                    Log.d(TAG, "playing: ${fav.preview}")

                    listenerView.onTitleSelected(holder, fav)
                }

            }
        }

        holder.binding.fav.setOnClickListener {
                holder.binding.fav.setImageResource(R.drawable.coeurvide)
                it.tag = "coeurvide"
                //suppression de la bdd
            AppApplication.applicationScope.launch {
                AppApplication.database.trackDAO().delete(fav)
                Log.d(DashboardFragment.TAG, "Size tracks: " + AppApplication.database.trackDAO().getAllTracks().size)
            }


        }


}
    
    class ListenerView(private val adapter: FavRecycleViewAdapter, private val recyclerView: RecyclerView, private val mediaPlayer: MediaPlayer) {
        var currentPlaying: FavRecycleViewAdapter.FavViewHolder? = null

        fun onTitleSelected(newHolder: FavRecycleViewAdapter.FavViewHolder, track: Track) {
            // Mettez à jour l'image de tous les éléments en 'play'
            for (i in 0 until adapter.itemCount) {
                val holder =
                    recyclerView.findViewHolderForAdapterPosition(i) as FavRecycleViewAdapter.FavViewHolder?
                holder?.binding?.audio?.setImageResource(R.drawable.play)
            }

            // Mettez à jour l'image du nouvel élément sélectionné en 'pause'
            newHolder.binding.audio.setImageResource(R.drawable.pause)

            // Mettez à jour le titre actuellement en cours de lecture
            currentPlaying = newHolder
        }

        fun onTitleStop() {
            // Mettez à jour l'image de tous les éléments en 'play'
            for (i in 0 until adapter.itemCount) {
                val holder =
                    recyclerView.findViewHolderForAdapterPosition(i) as FavRecycleViewAdapter.FavViewHolder?
                holder?.binding?.audio?.setImageResource(R.drawable.play)
            }

            // Mettez à jour le titre actuellement en cours de lecture
            currentPlaying = null
        }

        private fun stopCurrentPlaying() {
            // Si un média est en cours de lecture, arrêtez-le et mettez à jour son icône
            currentPlaying?.let {
                mediaPlayer.stop()
                mediaPlayer.reset()
                it.binding.audio.setImageResource(R.drawable.play)
            }
        }
    }
}
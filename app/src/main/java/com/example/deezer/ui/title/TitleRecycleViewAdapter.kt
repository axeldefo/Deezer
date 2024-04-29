package com.example.deezer.ui.title

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deezer.AppApplication
import com.example.deezer.R
import com.example.deezer.databinding.TitleRowBinding
import com.example.deezer.persistence.TrackDataBase
import com.example.deezer.service.data.Track
import com.example.deezer.ui.dashboard.DashboardFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TitleRecycleViewAdapter(
    private var titles: List<Track>,
    private val recyclerView: RecyclerView,
    mediaP: MediaPlayer = MediaPlayer()
) : RecyclerView.Adapter<TitleRecycleViewAdapter.TitleViewHolder>() {
    companion object {
        private const val TAG = "TitleRecycleViewAdapter"
    }
    val mediaPlayer = mediaP;

    init {
        Log.d(TAG, "TitleRecycleViewAdapter nbre de tracks: ${titles.size}")
    }

    inner class TitleViewHolder(val binding: TitleRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val binding = TitleRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TitleViewHolder(binding)
    }

    override fun getItemCount(): Int = titles.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {

        Log.d(TAG, "onBindViewHolder position=${position}")

        val track: Track = titles[position]
        Log.d(TAG, " titre ${track.title}")

        val listenerView = ListenerView(this, recyclerView, mediaPlayer)

        holder.binding.num.text = (position + 1).toString()
        holder.binding.titre.text = track.title

        val min = track.duration / 60
        val sec = track.duration % 60
        holder.binding.duree.text = "$min:$sec"

        holder.binding.audio.setOnClickListener {
            Log.d(TAG, "onClick: ${track.id}")


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
                    setDataSource(track.preview)
                    prepare() // might take long! (for buffering, etc)
                    start()
                    Log.d(TAG, "playing: ${track.preview}")

                    listenerView.onTitleSelected(holder, track)

                } else if (mediaPlayer.isPlaying && listenerView.currentPlaying != holder) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(track.preview)
                    prepare() // might take long! (for buffering, etc)
                    start()
                    Log.d(TAG, "playing: ${track.preview}")

                    listenerView.onTitleSelected(holder, track)
                }

            }
        }

        holder.binding.fav.setOnClickListener {
            Log.d(TAG, "onClick: ${track.id}")
            if (holder.binding.fav.tag == "coeurplein") {
                holder.binding.fav.setImageResource(R.drawable.coeurvide)
                it.tag = "coeurvide"
                Log.d(TAG, "coeurvide")
                AppApplication.applicationScope.launch {
                    AppApplication.database.trackDAO().delete(track)
                    Log.d(DashboardFragment.TAG, "Insert track: ${track.title}")
                }
            } else {
                holder.binding.fav.setImageResource(R.drawable.coeurplein)
                it.tag = "coeurplein"
                AppApplication.applicationScope.launch {
                    AppApplication.database.trackDAO().insert(track)
                    Log.d(DashboardFragment.TAG, "Insert track: ${track.title}")
                }
                Log.d(TAG, "coeurplein")

            }

        }
    }



    class ListenerView(private val adapter: TitleRecycleViewAdapter, private val recyclerView: RecyclerView, private val mediaPlayer: MediaPlayer) {
        var currentPlaying: TitleRecycleViewAdapter.TitleViewHolder? = null

        fun onTitleSelected(newHolder: TitleRecycleViewAdapter.TitleViewHolder, track: Track) {
            // Mettez à jour l'image de tous les éléments en 'play'
            for (i in 0 until adapter.itemCount) {
                val holder =
                    recyclerView.findViewHolderForAdapterPosition(i) as TitleRecycleViewAdapter.TitleViewHolder?
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
                    recyclerView.findViewHolderForAdapterPosition(i) as TitleRecycleViewAdapter.TitleViewHolder?
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


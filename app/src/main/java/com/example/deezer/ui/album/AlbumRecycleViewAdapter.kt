package com.example.deezer.ui.album

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deezer.R
import com.example.deezer.databinding.AlbumRowBinding
import com.example.deezer.service.data.Album

class AlbumRecycleViewAdapter (private var albums: List<Album>, private val recyclerView: RecyclerView)
: RecyclerView.Adapter<AlbumRecycleViewAdapter.AlbumViewHolder>() {
    companion object {
        private const val TAG = "AlbumRecycleViewAdapter"
    }

    init {
        Log.d(TAG, "AlbumRecycleViewAdapter nbre d'albums: ${albums.size}")
    }


    inner class AlbumViewHolder(val binding: AlbumRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val binding = AlbumRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {

        Log.d(TAG, "onBindViewHolder position=${position}")

        val album: Album = albums[position]
        Log.d(TAG, " artist ${album.cover_big}")


        holder.binding.album.text = "Album: " + album.title
        holder.binding.date.text = "Date: " + album.release_date
        Glide.with(holder.binding.imageView.context)
            .load(album.cover_big)
            .into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            Log.d(TAG, "onClick: ${album.id}")

            val args = bundleOf("album" to album.id, "cover" to album.cover_big, "title" to album.title, "date" to album.release_date)
            val navController = recyclerView.findNavController()
            navController.navigate(R.id.navigation_title, args)

        }
    }
}
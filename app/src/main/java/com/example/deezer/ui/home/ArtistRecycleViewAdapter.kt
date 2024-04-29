package com.example.deezer.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deezer.R
import com.example.deezer.databinding.ArtistRowBinding
import com.example.deezer.service.data.Artist

class ArtistRecycleViewAdapter(private var artists: List<Artist>, private val recyclerView: RecyclerView)
    : RecyclerView.Adapter<ArtistRecycleViewAdapter.ArtistViewHolder>() {
    companion object {
        private const val TAG = "ArtistRecycleViewAdapter"
    }


    init {
        Log.d(TAG, "ArtistRecycleViewAdapter : ${artists.size}")
    }


    inner class ArtistViewHolder(val binding: ArtistRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val binding = ArtistRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun getItemCount(): Int = artists.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {


        Log.d(TAG, "onBindViewHolder position=${position}")

        val artist: Artist = artists[position]
        Log.d(TAG, " artist ${artist.picture}")



        holder.binding.nom.text = "Artiste: " + artist.name
        holder.binding.nbFans.text = "Fans: " + artist.nb_fan.toString()
        Glide.with(holder.binding.imageView.context)
            .load(artist.picture_big)
            .into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            Log.d(TAG, "onClick: ${artist.id}")

            val args = bundleOf("artist" to artist.id, "name" to artist.name)
            val navController = recyclerView.findNavController()
            navController.navigate(R.id.navigation_album, args)

        }
    }

}
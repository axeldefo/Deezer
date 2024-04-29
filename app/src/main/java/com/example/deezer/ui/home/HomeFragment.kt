package com.example.deezer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezer.databinding.FragmentHomeBinding
import com.example.deezer.service.CallSearchArtist
import com.example.deezer.service.DeezerService
import com.example.deezer.service.data.DataSearchArtist

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recycleArtist.layoutManager = LinearLayoutManager(context)
        binding.recycleArtist.adapter = ArtistRecycleViewAdapter(emptyList(), binding.recycleArtist)

        val artist = arguments?.getString("artist")
        val deezerService = DeezerService()

        artist?.let {
            Log.d("HomeFragment", "onCreateView: artist = $artist")
            deezerService.searchArtist(artist, object : CallSearchArtist() {
                override fun fireOnResponseOK(dataSearchArtist: DataSearchArtist) {
                    Log.d(TAG, "fireOnResponseOK: $dataSearchArtist")
                    _binding?.let {
                        it.recycleArtist.post(Runnable {
                            val adapter = ArtistRecycleViewAdapter(dataSearchArtist.data, it.recycleArtist)
                            it.recycleArtist.adapter = adapter
                        })
                    }
                }
            })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
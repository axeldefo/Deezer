package com.example.deezer.ui.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezer.databinding.FragmentAlbumBinding
import com.example.deezer.service.CallSearchAlbum
import com.example.deezer.service.DeezerService
import com.example.deezer.service.data.DataSearchAlbum
import com.example.deezer.ui.home.HomeViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recycleAlbum.layoutManager = LinearLayoutManager(context)
        binding.recycleAlbum.adapter = AlbumRecycleViewAdapter(emptyList(), binding.recycleAlbum)

        val artist = arguments?.getInt("artist")
        val deezerService = DeezerService()

        artist?.let {
            Log.d("AlbumFragment", "onCreateView: album = $artist")
            deezerService.searchAlbum(artist, object : CallSearchAlbum() {
                override fun fireOnResponseOK(dataSearchAlbum: DataSearchAlbum) {
                    Log.d(TAG, "fireOnResponseOK: $dataSearchAlbum")
                    _binding?.let {
                        it.recycleAlbum.post(Runnable {
                            val adapter =
                                AlbumRecycleViewAdapter(dataSearchAlbum.data, it.recycleAlbum)
                            it.recycleAlbum.adapter = adapter
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
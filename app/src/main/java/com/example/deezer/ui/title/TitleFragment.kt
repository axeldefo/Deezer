package com.example.deezer.ui.title

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.deezer.databinding.FragmentTitleBinding
import com.example.deezer.service.CallSearchTitle
import com.example.deezer.service.DeezerService
import com.example.deezer.service.data.DataSearchTitle
import com.example.deezer.ui.home.HomeViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TitleFragment : Fragment() {
    private var _binding: FragmentTitleBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentTitleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recycleTitle.layoutManager = LinearLayoutManager(context)
        binding.recycleTitle.adapter = TitleRecycleViewAdapter(emptyList(), binding.recycleTitle)

        val album = arguments?.getLong("album")
        val image = arguments?.getString("cover")
        val titleAlbum = arguments?.getString("title")
        val date = arguments?.getString("date")
        val deezerService = DeezerService()

        album?.let {
            Log.d("TitleFragment", "onCreateView: album = $album")

            deezerService.searchTitle(album, object : CallSearchTitle() {
                override fun fireOnResponseOK(dataSearchTitle: DataSearchTitle) {
                    _binding?.let {
                        it.albumName.post {
                            it.albumName.text = titleAlbum
                            it.releaseDate.text = date
                            image?.let {
                                Glide.with(requireContext())
                                    .load(image)
                                    .into(binding.albumImgView)

                            }
                        }
                        Log.d(TAG, "fireOnResponseOK: $dataSearchTitle")

                        it.recycleTitle.post(Runnable {
                            it.recycleTitle.adapter = TitleRecycleViewAdapter(dataSearchTitle.data, binding.recycleTitle)
                        })
                    }

                }
            })
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.recycleTitle.adapter.let {
            (it as TitleRecycleViewAdapter).mediaPlayer.stop()
        }

        _binding = null
    }
}
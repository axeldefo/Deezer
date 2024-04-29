package com.example.deezer.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezer.AppApplication
import com.example.deezer.databinding.FragmentDashboardBinding
import com.example.deezer.persistence.TrackDataBase
import com.example.deezer.service.data.Track
import com.example.deezer.ui.home.ArtistRecycleViewAdapter
import com.example.deezer.ui.title.TitleRecycleViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    companion object{
        val TAG: String = "DashboardFragment"
    }

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var favs = emptyList<Track>()

        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)


        AppApplication.applicationScope.launch {
            favs = AppApplication.database.trackDAO().getAllTracks()
            Log.d(TAG, "Size tracks: " + AppApplication.database.trackDAO().getAllTracks().size)
        }

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recycleFavoris.layoutManager = LinearLayoutManager(context)
        binding.recycleFavoris.adapter = FavRecycleViewAdapter(favs, binding.recycleFavoris)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recycleFavoris.adapter.let {
            (it as FavRecycleViewAdapter).mediaPlayer.stop()
        }
    }
}
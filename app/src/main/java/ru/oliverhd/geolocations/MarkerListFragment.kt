package ru.oliverhd.geolocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.oliverhd.geolocations.databinding.FragmentMarkerListBinding

class MarkerListFragment : Fragment() {

    private var _binding: FragmentMarkerListBinding? = null
    private val binding: FragmentMarkerListBinding
        get() = _binding!!
    private val adapter = MarkerListRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarkerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.markerListRecyclerView.adapter = adapter
        adapter.setList(MARKER_LIST)
    }

    companion object {
        fun newInstance() = MarkerListFragment()
        val MARKER_LIST: MutableList<Marker> = mutableListOf()
    }
}
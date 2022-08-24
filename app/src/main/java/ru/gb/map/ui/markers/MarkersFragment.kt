package ru.gb.map.ui.markers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.map.ViewModelSaver
import ru.gb.map.databinding.FragmentMarkersBinding
import ru.gb.map.ui.MainViewModel

class MarkersFragment : Fragment() {

    private var _binding: FragmentMarkersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (requireActivity() as ViewModelSaver).getViewModel()
            //ViewModelProvider(this)[MainViewModel::class.java]
        _binding = FragmentMarkersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager
                .VERTICAL, false
        )
        val adapter = Adapter { viewModel.deletePlaceMarker(it) }
        recyclerView.adapter = adapter
        viewModel.liveDataPlaceMarkersList.observe(viewLifecycleOwner) {
            adapter.setMarkerData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
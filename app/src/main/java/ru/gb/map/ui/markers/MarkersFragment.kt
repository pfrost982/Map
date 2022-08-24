package ru.gb.map.ui.markers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.MainScope
import ru.gb.map.databinding.FragmentMarkersBinding
import ru.gb.map.ui.MainViewModel

class MarkersFragment : Fragment() {

    private var _binding: FragmentMarkersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val markersViewModel =
            ViewModelProvider(this)[MainViewModel::class.java]

        _binding = FragmentMarkersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMarkers
        markersViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}